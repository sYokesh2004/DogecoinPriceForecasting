# -*- coding: utf-8 -*-
import pandas as pd
import numpy as np
from sklearn.preprocessing import MinMaxScaler
from xgboost import XGBRegressor
from sklearn.multioutput import MultiOutputRegressor
import yfinance as yf
from datetime import datetime
from sqlalchemy import create_engine
import sys

print("Starting Dogecoin Forecast Script...")

# Set start date and dynamically get today's date
start_dt = "2018-12-01"
end_dt = datetime.today().strftime('%Y-%m-%d')

try:
    ticker = ["DOGE-USD"]
    print(f"Downloading data for {ticker} from {start_dt} to {end_dt} ...")
    df = yf.download(ticker, start=start_dt, end=end_dt)
    if df.empty:
        print("Error: Downloaded DataFrame is empty! Exiting.")
        sys.exit(1)
    df.columns = df.columns.droplevel(1)
    print("Data download successful.")
except Exception as e:
    print(f"Error during data download: {e}")
    sys.exit(1)

try:
    for i in range(30):
        print(f"Forecast iteration {i+1}/30...")
        features = df[['Open', 'High', 'Low', 'Volume', 'Close']]
        target = df[['Open', 'High', 'Low', 'Volume', 'Close']]

        scaler = MinMaxScaler()
        features_scaled = scaler.fit_transform(features)
        target_scaled = scaler.fit_transform(target)

        train_size = int(len(features_scaled) * 0.99)
        X_train, X_test = features_scaled[:train_size], features_scaled[train_size:]
        y_train, y_test = target_scaled[:train_size], target_scaled[train_size:]

        model = MultiOutputRegressor(
            XGBRegressor(objective='reg:squarederror', n_estimators=100, learning_rate=0.99)
        )
        model.fit(X_train, y_train)

        predictions_scaled = model.predict(X_test)
        predictions = scaler.inverse_transform(predictions_scaled)
        y_test_original = scaler.inverse_transform(y_test)

        results = pd.DataFrame(predictions, columns=['Open', 'High', 'Low', 'Volume', 'Close'])
        results['Date'] = df.index[train_size:]
        results['Actual Open'] = y_test_original[:, 0]
        results['Actual High'] = y_test_original[:, 1]
        results['Actual Low'] = y_test_original[:, 2]
        results['Actual Volume'] = y_test_original[:, 3]
        results['Actual Close'] = y_test_original[:, 4]

        last_features = features_scaled[-1].reshape(1, -1)
        next_day_prediction_scaled = model.predict(last_features)
        next_day_prediction = scaler.inverse_transform(next_day_prediction_scaled)

        next_day = pd.DataFrame(next_day_prediction, columns=['Open', 'High', 'Low', 'Volume', 'Close'])
        next_day['Date'] = [df.index[-1] + pd.Timedelta(days=1)]
        next_day.set_index('Date', inplace=True)
        df = pd.concat([df, next_day])
    print("Forecasting completed.")
except Exception as e:
    print(f"Error during forecasting/modeling: {e}")
    sys.exit(1)

# Direct Supabase connection link
connection_url = "postgresql+psycopg2://postgres:sasikumar23@db.zwcmmwdepxwdfdvxtamb.supabase.co:5432/postgres"

# Create SQLAlchemy engine
engine = create_engine(connection_url)

try:
    forecast_data = df.tail(30).copy()
    forecast_data.reset_index(inplace=True)

    print("Saving forecast data to database...")
    forecast_data.to_sql(name='forecast_table', con=engine, if_exists='replace', index=False)
    print("Last 30 days forecast saved to supabase table `forecast_table`")
except Exception as e:
    print(f"Error saving to database: {e}")
    sys.exit(1)

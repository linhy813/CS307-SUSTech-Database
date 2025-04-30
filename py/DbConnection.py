import configparser
import os
import psycopg2


class DBConnection:

    def __init__(self):
        self.connection = None
        self.cursor = None

    def open_db(self, config):
        try:
            parameters = {
                'host': config['DEFAULT']['host'],
                'database': config['DEFAULT']['database'],
                'user': config['DEFAULT']['user'],
                'password': config['DEFAULT']['password'],
                'port': config['DEFAULT']['port']
            }

            self.connection = psycopg2.connect(**parameters)
            self.cursor = self.connection.cursor()

            if not self.connection is None:
                print(f"Successfully connected to the database {parameters['database']} as {parameters['user']}")
                self.connection.autocommit = False

        except psycopg2.Error as e:
            raise RuntimeError("Connection Failed. ")

    def load_properties(self):
        config = configparser.ConfigParser()
        try:
            if not os.path.exists("../resources/dbUser.properties"):
                raise FileNotFoundError(f"Cannot find properties file: resources/dbUser.properties")

            config.read("../resources/dbUser.properties")
            if not config['DEFAULT']:
                raise ValueError("Properties file is empty or invalid. ")

            return config

        except (FileNotFoundError, ValueError) as e:
            print(f"Error: {e}")
            raise

    def close_db(self):
        if not self.connection is None:
            if not self.cursor is None:
                self.cursor.close()
            self.connection.close()
            self.connection = None

    def get_connection(self):
        if self.connection is None:
            raise RuntimeError("Connection not opened.")
        return self.connection

    def get_stmt(self):
        return self.cursor

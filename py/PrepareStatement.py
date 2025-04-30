import datetime
import math
import re
import string
import DbConnection
import psycopg2
import DataType
import SqlSentences


class PrepareStatement:
    def __init__(self, DBcon: DbConnection, sql: SqlSentences):
        self.DBcon = DBcon
        self.sql_manager = SqlSentences.SQLSentencesManager()
        self.sql = self.sql_manager.sql_sentences_map[sql]
        self.con = self.DBcon.get_connection()
        self.cursor = None
        self.params = []
        self.dict = {}
        self.bindCursor()

    def bindCursor(self):
        try:
            self.cursor = self.con.cursor()
        except psycopg2.Error as e:
            print("Connection failed. ")
            raise RuntimeError(e)

    def insertData(self, data: string, type: DataType, place: int):
        if self.con:
            try:
                if not isinstance(place, int) or place < 1:
                    raise ValueError("Place must be an integer greater than or equal to 1.")

                match type:
                    case DataType.DataType.INT:
                        self.dict[place] = int(data)
                    case DataType.DataType.STRING:
                        self.dict[place] = data
                    case DataType.DataType.DATE:
                        if data is None:
                            self.dict[place] = None
                            return
                        parts = re.split("-", data)
                        year = int(parts[0])
                        month = int(parts[1])
                        day = int(parts[2])
                        self.dict[place] = datetime.date(year, month, day)

            except psycopg2.Error as e:
                raise RuntimeError(e)

        else:
            print("No connection")
            exit(1)

    def addIntoList(self):
        self.params.append(tuple(self.dict.values()))

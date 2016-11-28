from storage.DBManager import DBManager
from ml.Oracol import Oracol
from ml.BigEye import BigEye

if __name__ == '__main__':
    dbm = DBManager()
    oracol = Oracol()
    big_eye = BigEye(dbm, oracol)
    big_eye.run()

import socket
import sys
import os
import json

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_address = ('localhost', 10000)
print('Starting up on %s port %s' %server_address)
sock.bind(server_address)
sock.listen(1)

dataDictionary = {}
dataTuple = []
content = []

def startLoad():
    tempTuple = []
    with open("data.txt") as f:
        for line in f:
            tempLine = line.strip("\n")
            temp = line.split("|")
            if not len(temp) < 4:
                temp[0] = (temp[0].strip()).capitalize()
                if len(temp[0]) != 0:
                    if not temp[0] in dataDictionary.keys():
                        temp[1] = temp[1].strip()
                        if temp[1].isdigit() or temp[1] == "":
                            temp[2] = temp[2].strip()
                            temp[3] = temp[3].strip() + "\n"
                            tempTuple.append(tuple(temp))
                            dataDictionary[temp[0]] = temp[1:]
        dataTuple = tempTuple

if __name__ == '__main__':
    startLoad()

def findFun(s):
    name = s.strip()
    if len(name) == 0:
        return "Invalid input"
    else:
        if name in dataDictionary.keys():
            return name + "|" + dataDictionary[name][0] + "|" + dataDictionary[name][1] + "|" + dataDictionary[name][2]
        else:
            return "Customer not found"

def writeFile(dataSet):
    f = open("data.txt", "w")
    lineSet = []
    for keys, values in dataSet.items():
        s = keys + "|" + values[0] + "|" + values[1] + "|" + values[2]
        lineSet.append(s)
    f.writelines(lineSet)
    f.close()

def addFun(s):
    checkString = s.split("|")
    checkString[0] = checkString[0].strip()
    if len(checkString[0]) == 0:
        return "Invalid input"
    else:
        if checkString[0] in dataDictionary.keys():
            return "Customer already exist"
        else:
            checkString[1] = checkString[1].strip()
            checkString[2] = checkString[2].strip()
            checkString[3] = checkString[3].strip() + "\n"
            dataDictionary.update({checkString[0]: checkString[1:]})
            writeFile(dataDictionary)
            return "Customer has been added"

def deleteFun(s):
    name = s.strip()
    if (len(name) == 0):
        return "Invalid input"
    else:
        if name in dataDictionary.keys():
            del dataDictionary[name]
            writeFile(dataDictionary)
            return "Customer has been deleted"
        else:
            return "Customer does not exist"

def ageFun(s):
    checkString = s.split("|")
    checkString[0] = checkString[0].strip()
    if len(checkString[0]) == 0:
        return "Invalid input"
    else:
        name = checkString[0]
        age = checkString[1].strip()
        if name in dataDictionary.keys():
            dataDictionary[name][0] = age
            writeFile(dataDictionary)
            return "Cusomer age has been updated"
        else:
            return "Customer does not exist"

def addressFun(s):
    checkString = s.split("|")
    checkString[0] = checkString[0].strip()
    if len(checkString[0]) == 0:
        return "Invalid input"
    else:
        name = checkString[0]
        address = checkString[1].strip()
        if name in dataDictionary.keys():
            dataDictionary[name][1] = address
            writeFile(dataDictionary)
            return "Cusomer address has been updated"
        else:
            return "Customer does not exist"

def phoneFun(s):
    checkString = s.split("|")
    checkString[0] = checkString[0].strip()
    if len(checkString[0]) == 0:
        return "Invalid input"
    else:
        name = checkString[0]
        phone = checkString[1].strip() + "\n"
        if name in dataDictionary.keys():
            dataDictionary[name][2] = phone
            writeFile(dataDictionary)
            return "Cusomer phone has been updated"
        else:
            return "Customer does not exist"

def reportFun():
    sTemp = ''
    for key in sorted(dataDictionary.keys()):
        sTemp += key + "|"
        if dataDictionary[key][0] != "":
            sTemp += dataDictionary[key][0]
        sTemp += "|"
        if dataDictionary[key][1] != "":
            sTemp += dataDictionary[key][1]
        sTemp += "|"
        if dataDictionary[key][2] != "":
            sTemp += dataDictionary[key][2]
        else:
            sTemp += "\n"
    return sTemp

def callFun(argument, s):
    if(argument == 1):
        return findFun(s)
    elif(argument == 2):
        return addFun(s)
    elif (argument == 3):
        return deleteFun(s)
    elif (argument == 4):
        return ageFun(s)
    elif (argument == 5):
        return addressFun(s)
    elif (argument == 6):
        return phoneFun(s)
    elif (argument == 7):
        return reportFun()

while True:
    print('\nWaiting for a connection')
    connection, client_address = sock.accept()
    try:
        print('\nConnection from ', client_address)
        while True:
            data = connection.recv(1024)
            s = data.decode('utf-8')
            no = int(s[:1])
            print('\nReceived : ' + str(data))
            if no > 0 and no < 8:
                s = s[2:]
                print('Sending data back to the client')
                connection.sendall(callFun(no, s).encode('utf-8'))
            else:
                print('\nNo more data from ', client_address)
                break
    finally:
        connection.close()
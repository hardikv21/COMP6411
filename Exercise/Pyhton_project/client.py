import socket
import sys

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_address = ('localhost', 10000)
print('Connecting to %s port %s' % server_address)
sock.connect(server_address)

def checkChoice(choice):
    if choice == "1" or choice == "3":
        name = input("Enter name : ").capitalize()
        return choice + "|" + name
    elif choice == "2":
        name = input("Enter name : ").capitalize()
        age = ''
        while True:
            age = input("Enter age : ")
            age = age.strip()
            if age.isdigit():
                break
            elif age == "":
                break
            else:
                print("Not an integer")
                continue
        address = input("Enter address : ")
        phone = input("Enter phone : ")
        return choice + "|" + name + "|" + age + "|" + address + "|" + phone
    elif choice == "4":
        name = input("Enter name : ").capitalize()
        while True:
            age = input("Enter age : ")
            age = age.strip()
            if age.isdigit():
                return choice + "|" + name + "|" + age
            elif age == "":
                return choice + "|" + name + "|" + age
            else:
                print("Not an integer")
                continue
    elif choice == "5":
        name = input("Enter name : ").capitalize()
        address = input("Enter address : ")
        return choice + "|" + name + "|" + address
    elif choice == "6":
        name = input("Enter name : ").capitalize()
        phone = input("Enter phone : ")
        return choice + "|" + name + "|" + phone
    elif choice == "7":
        return choice
    elif choice == "8":
        return "stop"
    else:
        return "Invalid choice"

try:
    choice = 0;
    display = '\nPython DB Menu\n\n1. Find customer\n2. Add customer\n3. Delete customer\n4. Update customer age\n' \
              '5. Update customer address\n6. Update customer phone\n7. Print report\n8. Exit\n'
    while (choice != 8):
        print(display)
        choice = input("Enter choice : ")
        data = checkChoice(choice)
        if data == "Invalid choice":
            print("\nInvalid choice")
        elif data == "stop":
            print("Stop!!")
            data = "0"
            sock.sendall(data.encode('utf-8'))
            break
        else:
            sock.sendall(data.encode('utf-8'))
            data_received = sock.recv(1024)
            print("\n" + data_received.decode('utf-8'))

finally:
    print('Closing socket')
    sock.close()
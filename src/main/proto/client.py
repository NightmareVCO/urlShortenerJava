import grpc
import UrlRn_pb2
import UrlRn_pb2_grpc

from google.protobuf.json_format import MessageToJson

channel = grpc.insecure_channel('localhost:8080')
stub = UrlRn_pb2_grpc.UrlRnStub(channel)

menu = True
while menu:

    print("Menu: ")
    print("1. Get URL by username: ")
    print("2. Add URL for a username: ")
    print("3. Exit")
    choice = int(input("Enter your choice: "))

    if choice == 1:
        username = input("Enter the username: ")
        user = UrlRn_pb2.UserRequest(username=username)
        response = stub.listUserUrls(user)
       
        for url in response.urls:
            print(f"URL: {url.originalUrl}")
            print(f"Short URL: {url.url}")
            print(f"Created At: {url.date}")
            for statistic in url.statistic.statistic:
                print(f"Browser: {statistic.browser}")
                print(f"Operating System: {statistic.operatingSystem}")
                print(f"IP Direction: {statistic.ipDirection}")
        
    elif choice == 2:
        username = input("Enter the username: ")
        url = input("Enter the URL: ")
        create = UrlRn_pb2.UrlRequest(originalUrl=url, username=username)
        response = stub.createUrl(create)
    

    elif choice == 3:
        menu = False
    else:
        print("Invalid choice")
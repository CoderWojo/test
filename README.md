Chatting App using Sockets in Java

Project Description:
This is a simple chat application built using Java Sockets. It allows real-time communication between multiple clients through a server. 
The server relays messages from one client to all other connected clients, creating a chat room environment.

Features
- Support for multiple clients,
- Real-time messaging,
- Simple server-client communication using TCP/IP sockets,
- Easy setup and usage,
- System Requirements,
- Java SE 8 or higher.

Installation
Clone the repository to your local machine:

(Copy the code):
  git clone https://github.com/CoderWojo/Chatting-app-using-Socket-s-in-Java.git
  cd Chatting-app-using-Socket-s-in-Java

Running the Application
  Start the Server:

Run the Server.java file to start the server.

(Copy the code):
  java Server
  
Start Clients:
Run the Client.java file in separate terminals or instances for each client that you want to connect to the server.

(Copy the code):
  java Client

How It Works
Server: Listens for connections on a specific port (default is port 9999). When a client connects, the server manages messages and broadcasts them to all other clients.
Client: Sends messages to the server, which are then distributed to all connected clients.
Project Structure

├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── Server.java  # Server-side code
│   │   │   ├── Client.java  # Client-side code

Future Development
- Add a graphical user interface (GUI),
- Encrypt messages for security,
- Implement user authentication.

Author
GitHub Username: CoderWojo,
<<<<<<< HEAD
Contact: wojciechwazny444@gmail.com.
=======
Contact: wojciechwazny444@gmail.com.
>>>>>>> b7d549f9ae805a02e13bb3296da4e9da5de66460

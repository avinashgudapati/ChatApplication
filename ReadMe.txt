UnZip and import project to eclipse.


To run the application:
----------------------------
-> First Run the Server.java in ServerPackage
-> then Run Login.java in ClientPackage
-> Login asks for username and this username(can be anything ex: "Avinash" ,"Rahath") will the chat windows identifier.
-> one can run login.java multiple times to add multiple clients to group chat.
	(With different username each time to distinguish users)
-> After username is entered and login button is presses. client gui is displayed
-> Client gui has chatting messages(to display chat), online users in chat
-> also message box and respective send button to sent messages to group
-> Logout to log out from group chat(even one user log out others can continue chat)
-> Browse button to browse files from local.
-> on selecting a file in browse button that file will uploaded to server and stored in "Chat Application" directory.
-> "Chat Application" directory is where our project's bin and src files lie.
-> A successful file upload message will be displayed on file upload.
-> any type of files can be sent to server(.doc,.txt,.png anything) 
-> on user logout his name will be removed from online users list. and logged out message will be displayed on chat window.
-> Multiple users can send messages at a time(Multi Threading is effectively used to handle them).

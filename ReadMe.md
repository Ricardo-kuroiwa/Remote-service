Abrir o terminal no raiz do projeto
javac compute/Compute.java compute/Task.java
jar cvf classes/compute.jar compute/*.class
javac -cp ./classes/compute.jar engine/ComputeEngine.java
javac -cp ./classes/compute.jar   client/Services.java
javac -cp ./classes/compute.jar tcp/TCPClient.java
javac  ./tcp/TCPClient.java ./tcp/TCPServer.java  

Execute rmiregistry
Abra outro terminal e execute java engine.ComputeEngine localhost 1099
Abra outro terminal e  execute java tcp.TCPServer localhost 1099    
Abra outro terminal e  execute cd ./tcp
execute  java TCPClient localhost

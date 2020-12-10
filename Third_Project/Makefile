port = 4644

all: compile doc scripts

compile: HTTP1Server.java Client.java
	javac HTTP1Server.java Client.java

doc: doc_root.tar-1-1.gz
	tar -zxvf doc_root.tar-1-1.gz

scripts: cgi_bin.zip
	unzip cgi_bin.zip

format: doc_root cgi_bin
	mv cgi_bin doc_root

copy1: HTTP1Server.class
	mv HTTP1Server.class ./doc_root

copy2: client_handler.class
	mv client_handler.class ./doc_root

clean:
	rm -r doc_root Client.class cgi_bin client_handler.class HTTP1Server.class

server:
	java -cp . HTTP1Server $(port)

tester:
	java -jar HTTPServerTester.jar localhost $(port)

client:
	java Client $(port)

old: compile doc copy1 copy2

oldtester:
	java -jar HTTPServerTester-1-1.jar localhost $(port)
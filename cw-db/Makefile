clean:
	rm -r ../databases/*

compile:
	./mvnw clean compile

server:
	./mvnw clean compile exec:java@server

client:
	./mvnw exec:java@client
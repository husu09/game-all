cd /d %~dp0
protoc --java_out=src\main\java proto\*.proto
protoc --proto_path=F:\\workspace\\game-all\\game-proto --csharp_out=F:\workspace\game-all\game-proto\csharp-proto *.proto
pause
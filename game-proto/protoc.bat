cd /d %~dp0
protoc --proto_path=proto --java_out=src\main\java proto\*.proto
pause
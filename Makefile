run:
	mvn exec:java -Dexec.mainClass="com.furnadelampiao.Main"

db-up:
	docker compose up -d

db-down:
	docker compose down -v

db-logs:
	docker compose logs -f postgres

seed:
	mvn compile exec:java "-Dexec.mainClass=com.furnadelampiao.seed.SeedRunner"


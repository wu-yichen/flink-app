.PHONY: clean build-jar full-run run
.DEFAULT_GOAL := help

clean: ## Deletes all generated files (in the target directory)
	@sbt clean

build-jar: ## Build a fat jar with sbt assembly
build-jar: clean
	@sbt assembly

full-run: ## Run the application with a new-built fat jar file
full-run: build-jar
	./run.sh


run: ## Run the application without building a fat jar when no source code change
	./run.sh

help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

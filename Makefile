.PHONY: setup
setup:
	echo "Welcome timez app"
	cp secrets.default.properties secrets.properties
	brew install ktlint

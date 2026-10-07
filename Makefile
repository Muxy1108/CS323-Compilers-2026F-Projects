ANTLR_JAR ?= libs/antlr-4.13.2-complete.jar

JAVA_BUILD_FLAGS ?= -Xms16m -Xmx256m -XX:MaxMetaspaceSize=128m -XX:+UseSerialGC
JAVA_TOOL_BUILD_FLAGS := $(addprefix -J,$(JAVA_BUILD_FLAGS))

GRAMMAR := Splc.g4
GENERATED_DIR := src/main/java/generated
BIN_DIR := bin
CLASS_DIR := $(BIN_DIR)/classes
TARGET_DIR := target
JAR_FILE := $(TARGET_DIR)/project2.jar
HANDIN_FILE := $(TARGET_DIR)/project2.zip

TEST_IN_DIR := testcases/project2/in
TEST_OUT_DIR := testcases/project2/out

.PHONY: all generate compile build run test handin clean

all: build

generate:
	@rm -rf $(GENERATED_DIR)
	@mkdir -p $(GENERATED_DIR)
	java $(JAVA_BUILD_FLAGS) -jar $(ANTLR_JAR) \
		-visitor \
		-no-listener \
		-package generated \
		-o $(GENERATED_DIR) \
		$(GRAMMAR)

compile: generate
	@rm -rf $(CLASS_DIR)
	@mkdir -p $(CLASS_DIR)
	javac $(JAVA_TOOL_BUILD_FLAGS) \
		-cp $(ANTLR_JAR) \
		-d $(CLASS_DIR) \
		$$(find src/main/java -name "*.java")

build: compile
	@mkdir -p $(TARGET_DIR)
	jar $(JAVA_TOOL_BUILD_FLAGS) --create \
		--file $(JAR_FILE) \
		--main-class Main \
		-C $(CLASS_DIR) .

run: build
	java \
		-Xms16m \
		-Xmx128m \
		-XX:MaxMetaspaceSize=128m \
		-cp $(JAR_FILE):$(ANTLR_JAR) \
		Main

test: build
	@set -e; \
	found=0; \
	passed=0; \
	total=0; \
	for input in $(TEST_IN_DIR)/*.splc; do \
		[ -e "$$input" ] || continue; \
		found=1; \
		total=$$((total + 1)); \
		name=$$(basename "$$input" .splc); \
		expected="$(TEST_OUT_DIR)/$$name.out"; \
		if [ ! -f "$$expected" ]; then \
			echo "[MISSING] $$expected"; \
			exit 1; \
		fi; \
		actual=$$(mktemp); \
		java \
			-Xms16m \
			-Xmx128m \
			-XX:MaxMetaspaceSize=128m \
			-cp $(JAR_FILE):$(ANTLR_JAR) \
			Main < "$$input" > "$$actual"; \
		if diff --strip-trailing-cr -u "$$expected" "$$actual" > /dev/null; then \
			echo "[PASS] $$name"; \
			passed=$$((passed + 1)); \
		else \
			echo "[FAIL] $$name"; \
			echo "----------------------------------------"; \
			diff --strip-trailing-cr -u "$$expected" "$$actual" || true; \
			echo "----------------------------------------"; \
		fi; \
		rm -f "$$actual"; \
	done; \
	if [ "$$found" -eq 0 ]; then \
		echo "No testcases found in $(TEST_IN_DIR)."; \
		exit 1; \
	fi; \
	echo; \
	echo "Result: $$passed / $$total passed"; \
	if [ "$$passed" -ne "$$total" ]; then \
		exit 1; \
	fi

handin: test
	@mkdir -p $(TARGET_DIR)
	@rm -f $(HANDIN_FILE)
	jar $(JAVA_TOOL_BUILD_FLAGS) --create \
		--file $(HANDIN_FILE) \
		--no-manifest \
		-C . Splc.g4 \
		-C src/main/java ConstExprVisitor.java \
		-C src/main/java/framework/project2 Project2ErrorStrategy.java
	@echo "Created $(HANDIN_FILE)"
	@echo "Contents:"
	@jar tf $(HANDIN_FILE)

clean:
	rm -rf $(GENERATED_DIR) $(BIN_DIR) $(TARGET_DIR)

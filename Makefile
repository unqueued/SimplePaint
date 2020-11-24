NAME=SimplePaint
JC = javac
JFLAGS = -g

default:
	$(JC) $(JFLAGS) *.java

clean:
	$(RM) *.class *.jar

jar: default
	jar cvfe $(NAME).jar $(NAME) *.class
	rm *.class



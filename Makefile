all: 
	 javac *.java

compiler: 
	 javac *.java

clean:
	 rm -f -r *.class 
	 rm -f -r ast/*.class 
	 rm -f -r intermediaryGeneration/*.class 
	 rm -f -r parser/*.class 
	 rm -f -r semanticAnalyzer/*.class
	 rm -f -r visitor/*.class


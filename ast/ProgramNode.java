package ast;

import java.io.FileNotFoundException;
import java.util.LinkedList;

import semanticAnalyzer.SemanticException;
import visitor.Visitor;

import ast.basic.Node;
import ast.functions.FunctionNode;

public class ProgramNode implements Node {

   private MainNode mainNode;
   private LinkedList<FunctionNode> functionNodes;

   public ProgramNode(MainNode mainNode, LinkedList<FunctionNode> functionNodes) {
      this.mainNode = mainNode;
      this.functionNodes = functionNodes;
   }

   @Override
   public void acceptVisitor(Visitor v) throws SemanticException,
         FileNotFoundException {
      v.visitProgramNode(mainNode, functionNodes);
   }

}

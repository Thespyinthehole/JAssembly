interface Instruction<T> {
  
   void action(T t) throws IntepretException;
}

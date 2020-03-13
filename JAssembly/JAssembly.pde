Program p;
CPU cpu = new CPU(20, 8);
void setup() {
  String[] program = loadStrings("Program.txt");
  try {
    p = new Program(program);
    cpu.loadIntoMemory(p);
    println(cpu);
  } 
  catch (SyntaxException e) {
    println(e);
    p = null;
  }
}

int index = 1;
void draw() {
  if(cpu.halted)
    return;
  cpu.step();
  println("Step " + index++ + ": ");
  println(cpu);
}

Program p;
CPU cpu = new CPU(20,8);
void setup() {
  String[] program = loadStrings("Program.txt");
  try {
    p = new Program(program);
    cpu.loadIntoMemory(p);
    cpu.run();
  } 
  catch (SyntaxException e) {
    println(e);
    p = null;
  }
}

void draw() {
}

class Program {
  
  Line[] instructions;
  HashMap<String, String> identifiers = new HashMap<String, String>();

  Program(String[] program) throws SyntaxException {
    ArrayList<Line> lines = new ArrayList<Line>();

    short lineNum = 0;
    short memloc = 0;
    boolean variables = true;
    for (String instruction : program) {
      //Remove outside white space
      instruction = instruction.trim();

      if (variables) {
        if (instruction.contains("=")) {
          String[] parts = instruction.split("=");
          for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
          }
          if (parts.length != 2 || !parts[0].matches("[a-z]+") || !parts[1].matches("(r|(m[+-]?))?[0-9]+")) {
            throw new SyntaxException(lineNum + 1, "Invalid variable decleration");
          }
          if (identifiers.containsKey(parts[0]))
            throw new SyntaxException(lineNum + 1, "Duplicate identifier");
          identifiers.put(parts[0], parts[1]);
        } else {
          variables = false;
        }
      }

      if (!variables) {
        Line line = extractInstruction(instruction, lineNum, memloc);
        if (line != null) {
          lines.add(line);
          memloc += line.getMemorySize();
        }
      }


      lineNum++;
    }
    instructions = lines.toArray(new Line[0]);
  }

  Line extractInstruction(String instruction, int lineNum, int memloc) throws SyntaxException {
    //Remove comments
    int commentIndex = instruction.indexOf("#");
    if (commentIndex != -1)
      instruction = instruction.substring(0, commentIndex);

    //Get line name
    int index = instruction.indexOf(":");
    String lineName = null;

    //Line name exists
    if (index != -1) {
      //Extract line name
      lineName = instruction.substring(0, index).trim();

      //If line name is not all alphabatical characters invalid.
      if (!lineName.matches("[a-zA-z]+"))
        throw new SyntaxException(lineNum + 1, "Invalid line name");

      //Remove line name from line
      instruction = instruction.substring(index + 1).trim();
    }

    //Remove extra white space
    instruction.replace("\\s+", " ");


    //Do not process an empty line
    if (instruction.length() == 0 || instruction.equals(" ")) {
      //Check if the line name points to nothing
      if (lineName != null)
        throw new SyntaxException(lineNum + 1, "No instruction found, but line identifer exists.");
      return null;
    }

    //Add the line name to the list
    if (lineName != null) {
      if (identifiers.containsKey(lineName))
        throw new SyntaxException(lineNum + 1, "Duplicate identifier");
      identifiers.put(lineName, String.valueOf(memloc));
    }

    return new Line(instruction, lineNum);
  }

  void loadIntoMemory(short[] memory) throws SyntaxException {
    int index = 0;

    for (Line line : instructions) {
      for (short data : line.toBinary(identifiers)) 
        memory[index++] = data;
    }
  }
}

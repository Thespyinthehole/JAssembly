# Structure

This section will describe the main overview of how to structure the assembly code. It will talk about where to put different things and some basic knowledge about the language


The structure of the assembly code consists of two parts. The first part is the constant declaration, which is then followed by the main code base. This means that all your constant values need to be defined first, this is due to the way the language is compile in which is does two passes over the code. The first pass is to remove all comments and white spaces as well as replace all constants with their respective value, the second is then used to change the assembly code into byte code.

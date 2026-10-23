
public class Dialogue { 

	public static String checkCommand (String command) {
		switch(command) {
			case "help":
				return "HELP";
			case "use":
                                return "USE";

		}
		return "Command prompt not found among accepted. Type 'help' to view the controls.";
	}

	public static String interpret (String in) {
		String [] inputAry = in.split(":",2);
			if (!inputAry[0].equals("help") && inputAry.length != 2) {
				return "Command prompt not found. Type 'help' to view the controls.";
			} else {
				return checkCommand(inputAry[0]);
			}
	}
	public static void main (String [] args) {
		System.out.print("Should say NYRB: ");
		System.out.println(interpret("help"));
		System.out.print("Should say among accepted: ");
                System.out.println(interpret("search: room"));
		System.out.print("Should say not found: ");
                System.out.println(interpret("search the room"));
	}
}

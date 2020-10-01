// This class is a scanner for the program
// and programming language being interpreted.

import java.util.*;

public class Scanner {

	private String program; // source program being interpreted
	private int pos; // index of next char in program
	private Token token; // last/current scanned token

	// sets of various characters and lexemes
	private Set < String > whitespace = new HashSet < String > ();
	private Set < String > digits = new HashSet < String > ();
	private Set < String > letters = new HashSet < String > ();
	private Set < String > legits = new HashSet < String > ();
	private Set < String > keywords = new HashSet < String > ();
	private Set < String > operators = new HashSet < String > ();

	// initializers for previous sets

	/** 
	 * This method is responsible for filling the Scanner's lexicon for what valid tokens are.
	 * @param s This is the first paramater and specifies the Set to fill
	 * @param lo This is the second paramater and indicates the first character to fill the set with
	 * @param hi This is the third paramater and indicates the last character to add to the set
	 */
	private void fill(Set < String > s, char lo, char hi) {
		for (char c = lo; c <= hi; c++) // loop that begins at first character lo and ends after the final character hi. Increments c with each iteration.
			s.add(c + ""); // add the character to the Set
	}

	/**
	 * This method is responsible for adding tokens to the whitespace Set
	 * @param s This is the first parameter and specifies the Set to add each character to
	 */
	private void initWhitespace(Set < String > s) {
		s.add(" "); // Adds the empty space to the whitespace Set
		s.add("\n"); // Adds the newline character to the whitespace Set
		s.add("\t"); // Adds the tab character to the whitespace Set
	}

	/**
	 * This method uses the previously declared fill function to add a range of characters to the digits Set
	 * @param s This is the first parameter and specifies the Set to add each character to
	 */
	private void initDigits(Set < String > s) {
		fill(s, '0', '9'); // Pass the Set, lo('0'), and hi('9') paramaters for the fill function to add the characters in range to the Set
	}
	
	/**
	 * This method uses the previously declared fill function to add a range of characters to the letters Set
	 * @param s This is the first parameter and specifies the Set to add each character to
	 */
	private void initLetters(Set < String > s) {
		fill(s, 'A', 'Z'); // Pass A 
		fill(s, 'a', 'z');
	}

	/**
	 * This method is responsible for adding the letters and digits Set to the legits Set
	 * @param s This is the first parameter and specifies the set to add to
	 */
	private void initLegits(Set < String > s) {
		s.addAll(letters);
		s.addAll(digits);
	}

	/**
	 * This method is responsible for adding characters to the operators Set
	 * @param s This is the first parameter and specifies the set to add to
	 */
	private void initOperators(Set < String > s) {
		s.add("=");
		s.add("+");
		s.add("-");
		s.add("*");
		s.add("/");
		s.add("(");
		s.add(")");
		s.add(";");
	}

	private void initKeywords(Set < String > s) {}

	// constructor:
	//   - squirrel-away source program
	//   - initialize sets
	public Scanner(String program) {
		this.program = program;
		pos = 0;
		token = null;
		initWhitespace(whitespace);
		initDigits(digits);
		initLetters(letters);
		initLegits(legits);
		initKeywords(keywords);
		initOperators(operators);
	}

	// handy string-processing methods

	public boolean done() {
		return pos >= program.length();
	}

	private void many(Set < String > s) {
		while (!done() && s.contains(program.charAt(pos) + ""))
			pos++;
	}

	private void past(char c) {
		while (!done() && c != program.charAt(pos))
			pos++;
		if (!done() && c == program.charAt(pos))
			pos++;
	}

	// scan various kinds of lexeme

	private void nextNumber() {
		int old = pos;
		many(digits);
		token = new Token("num", program.substring(old, pos));
	}

	private void nextKwId() {
		int old = pos;
		many(letters);
		many(legits);
		String lexeme = program.substring(old, pos);
		token = new Token((keywords.contains(lexeme) ? lexeme : "id"), lexeme);
	}

	private void nextOp() {
		int old = pos;
		pos = old + 2;
		if (!done()) {
			String lexeme = program.substring(old, pos);
			if (operators.contains(lexeme)) {
				token = new Token(lexeme); // two-char operator
				return;
			}
		}
		pos = old + 1;
		String lexeme = program.substring(old, pos);
		token = new Token(lexeme); // one-char operator
	}

	// This method determines the kind of the next token (e.g., "id"),
	// and calls a method to scan that token's lexeme (e.g., "foo").
	public boolean next() {
		many(whitespace);
		if (done()) {
			token = new Token("EOF");
			return false;
		}
		String c = program.charAt(pos) + "";
		if (digits.contains(c))
			nextNumber();
		else if (letters.contains(c))
			nextKwId();
		else if (operators.contains(c))
			nextOp();
		else {
			System.err.println("illegal character at position " + pos);
			pos++;
			return next();
		}
		return true;
	}

	// This method scans the next lexeme,
	// if the current token is the expected token.
	public void match(Token t) throws SyntaxException {
		if (!t.equals(curr()))
			throw new SyntaxException(pos, t, curr());
		next();
	}

	public Token curr() throws SyntaxException {
		if (token == null)
			throw new SyntaxException(pos, new Token("ANY"), new Token("EMPTY"));
		return token;
	}

	public int pos() {
		return pos;
	}

	// for unit testing
	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(args[0]);
			while (scanner.next())
				System.out.println(scanner.curr());
		} catch (SyntaxException e) {
			System.err.println(e);
		}
	}

}

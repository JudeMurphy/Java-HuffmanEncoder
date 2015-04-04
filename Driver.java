/**
 * 
 * @author Jude Murphy
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class Driver 
{
	public static void main(String[] args) throws IOException 
	{
		Scanner askUser = new Scanner(System.in);
		System.out.print("(Default is testfile.txt) - Enter the text file name: ");
		String fileName = askUser.next();

		if(fileName.length() > 1)
		{
			long startTime = System.currentTimeMillis();

			List<alphabetRow> myAlphabet = new ArrayList<alphabetRow>();
			myAlphabet = getAlphabet(fileName);

			Forest[] myForest = new Forest[myAlphabet.size()];
			myForest = getForest(myAlphabet);

			HuffmanTreeNode[] Tree = createHTree(myForest);
			myAlphabet = setAlphabetCodes(myAlphabet, Tree);
			myAlphabet = reverseAlphabetCodesForTraversal(myAlphabet);
			myAlphabet = appendTo8Bits(myAlphabet);

			Zip(myAlphabet, fileName);

			String getCharacters = "";
			getCharacters = coding.decode("EncodedPrivateString.hzip");

			System.out.println("*** NEW FILES AND CYPHER CREATED ***");
			System.out.println("*** READING FROM .HZIP AND .HUFF FILES ***");

			System.out.println("STRING TO BE DECODED: " + getCharacters);

			String finalDecodedString = decipherCodes(getCharacters);
			System.out.println("DECODED STRING: " + finalDecodedString);

			long endTime = System.currentTimeMillis();
			long duration = (endTime - startTime); 
			System.out.println("TOTAL PROGRAM RUNNING TIME: " + duration + " milliseconds");
		}
		else
		{
			System.out.println("*** YOUR FILE NAME IS TOO SHORT, PLEASE TRY AGAIN ***");
		}
	}

	// ********************************* CREATING ALPHABET *********************************
	/*
	 * Call a getAlphabet() method, which opens the input file and creates an ALPHABET table containing an entry for each ASCII character encountered in the file. 
	 * Only characters actually appearing in the file must be entered in the ALPHABET table. 
	 * For each character, the frequency of that character must be recorded, which is the number of times the character appears divided by the total number of characters in the file. 
	 * The third field in the table must be the “leaf” field as discussed in class – initialized to the index of the character in the table.
	 */
	public static List<alphabetRow> getAlphabet(String fileName) throws FileNotFoundException
	{			
		// *** SIZE OF THE ASCII TABLE WITH EXTENDED CHARACTERS ***		
		List<alphabetRow> alphabetTable = new ArrayList<alphabetRow>();
		alphabetRow characterFromFile = new alphabetRow();

		// *** READING TEXT FILE ***
		FileReader fr = new FileReader(fileName);
		Scanner in = new Scanner(fr);

		HashMap<Character, Integer> alphabetMap = new HashMap<Character, Integer>();

		// *** WHILE TEXT FILE HAS NEXT LINE ***
		while(in.hasNextLine())
		{
			String Line = in.nextLine();
			System.out.println("BEFORE ENCODED STRING: " + Line);

			// *** GETTING CHARACTERS AT EACH LINE ***
			for(int i = 0; i < Line.length(); i++)
			{
				// *** CHECK ARRAY FOR CHARACTER AND INCREMENTING CHARACTER COUNT ***
				alphabetMap = findCharsInHashMap(Line.charAt(i), alphabetMap);
			}
		}

		for (Map.Entry<Character, Integer> entry : alphabetMap.entrySet()) 
		{
			alphabetRow alphabetEntry = new alphabetRow(entry.getKey(), entry.getValue(), 0, 0, "00000000");
			alphabetTable.add(alphabetEntry);
		}

		// *** GETTING TOTAL FREQUENCIES AND ADDING THEM UP ***
		double numOfTimesFound = 0;
		for(int i = 0; i < alphabetTable.size(); i++)
		{
			characterFromFile = alphabetTable.get(i);
			characterFromFile.setLeaf(i);
			numOfTimesFound = numOfTimesFound + characterFromFile.gettotalNum();
		}

		// *** ASSIGNING FREQUENCY VALUES TO OBJECTS ***
		for(int i = 0; i < alphabetTable.size(); i++)
		{
			characterFromFile = alphabetTable.get(i);
			characterFromFile.setweightedFrequency(characterFromFile.gettotalNum()/numOfTimesFound);
			alphabetTable.set(i, characterFromFile);
		}

		return alphabetTable;
	}

	public static HashMap<Character, Integer> findCharsInHashMap(char passedChar, HashMap<Character, Integer> map)
	{
		if(map.containsKey(passedChar))
		{
			map.put(passedChar, map.get(passedChar) + 1);
		}
		else
		{
			map.put(passedChar, 1);
		}

		return map;
	}

	public static List<alphabetRow> setAlphabetCodes(List<alphabetRow> myAlphabet, HuffmanTreeNode[] Tree)
	{
		for(int i = 0; i < myAlphabet.size(); i++)
		{	
			alphabetRow temp = myAlphabet.get(i);
			temp.setCode(getCodes(Tree, temp));
			myAlphabet.set(i, temp);
		}

		return myAlphabet;
	}

	public static List<alphabetRow> reverseAlphabetCodesForTraversal(List<alphabetRow> myAlphabet)
	{
		Stack<Character> reverseStack = new Stack<Character>();
		for(int i = 0; i < myAlphabet.size(); i++)
		{	
			alphabetRow temp = myAlphabet.get(i);
			String code = temp.getCode();
			String reversedCode = "";

			for(int j = 0; j < code.length(); j++)
			{
				reverseStack.push(code.charAt(j));
			}

			for(int n = 0; n < code.length(); n++)
			{
				reversedCode = reversedCode + reverseStack.pop();
			}

			temp.setCode(reversedCode);
			myAlphabet.set(i, temp);
		}

		return myAlphabet;
	}

	private static List<alphabetRow> appendTo8Bits(List<alphabetRow> myAlphabet) 
	{
		for(int i = 0; i < myAlphabet.size(); i++)
		{
			alphabetRow temp = myAlphabet.get(i);
			while(temp.getCode().trim().length() < 8)
			{
				temp.setCode(temp.getCode() + "0");
			}
			myAlphabet.set(i, temp);
		}
		return myAlphabet;
	}

	// ********************************* FINISHED CREATING ALPHABET *********************************

	// ********************************* CREATING FOREST *********************************
	/*
	 * The tree must be recorded in a TREE data structure, which is an array of 2n-1 elements where n is the number of distinct characters in the file. 
	 * Each entry in the TREE data structure must have a left-child, right-child, and parent fields, initially all set to -1. 
	 * As discussed in class, the algorithm also makes use of a FOREST data structure, which is an array with number of entries equal to the number of distinct characters in the ALPHABET data structure. 
	 * Each FOREST entry consists of two fields – a “frequency” field and a “root” field. The elements of the FOREST data structure must be initialized with information from the ALPHABET data structure. 
	 */
	public static Forest[] getForest(List<alphabetRow> newAlphabet)
	{
		Forest[] tempForest = new Forest[newAlphabet.size()];
		for(int i = 0; i < newAlphabet.size(); i++)
		{
			alphabetRow character = newAlphabet.get(i);
			Forest forestObj = new Forest(character.getweightedFrequency(), character.getLeaf());
			tempForest[i] = forestObj;
		}
		return tempForest;
	}

	public static boolean checkIfTreesExist(Forest[] forestParam)
	{	
		boolean treesExistInForest = false;
		outerloop: for(int i = 0; i < forestParam.length; i++)
		{
			Forest temp = forestParam[i];
			if(temp.getfrequency() < 1.0)
			{
				treesExistInForest = true;
				break outerloop;
			}
		}

		return treesExistInForest;
	}

	public static Forest[] findMins(Forest[] forestParam)
	{
		Arrays.sort(forestParam);

		Forest[] smallestCountingArray = new Forest[2];
		smallestCountingArray[0] = forestParam[0];
		smallestCountingArray[1] = forestParam[1];

		return smallestCountingArray;
	}

	public static int findIndexByRoot(Forest[] forestParam, Forest Forest)
	{
		int index = 0;
		outerloop: for(int i = 0; i < forestParam.length; i++)
		{
			Forest tempObj = forestParam[i];
			if(tempObj.getRoot() == Forest.getRoot())
			{
				index = i;
				break outerloop;
			}
		}

		return index;
	}
	// ********************************* FINISHED CREATING FOREST *********************************

	// ********************************* CREATING HUFFMAN TREE *********************************
	//*********************************************************************************************
	/*
	 * while (there are more than trees in the FOREST) {
	 * i = smallest_frequency_entry_in_FOREST
	 * j = second_smallest_frequency_entry_in_FOREST
	 * Update the lc field of the next available entry in TREE to the value from the root field of i Update the rc field of the new entry in TREE to the value from the root field of j
	 * Set the parent of the new entry in TREE to -1
	 * Set the parent fields of i and j in TREE to the index of the newly created  node
	 * Add the frequency of j to the frequency of i. 
	 * Set the root field of i in FOREST to the index of the new node in TREE
	 * “Delete” j by making its frequency larger than 1 or less than 0 (e.g. -1.0 or 2.0)
	 * }
	 */
	//*********************************************************************************************

	public static HuffmanTreeNode[] createHTree (Forest[] forestParam)
	{	
		HuffmanTreeNode[] Tree = new HuffmanTreeNode[(2 * forestParam.length - 1)];

		// *** FILLING TREE WITH -1 VALUES ***
		for(int i = 0; i < forestParam.length; i++)
		{
			HuffmanTreeNode fillerNode = new HuffmanTreeNode(-1, -1, -1);
			Tree[i] = fillerNode;
		}

		int currentTreeIndex = forestParam.length;

		boolean treesExistInForest = checkIfTreesExist(forestParam);		
		while(treesExistInForest)
		{
			// *** GETS SMALLEST INDEX FROM forestParam ARRAY ***
			Forest[] currentMinValues = findMins(forestParam);
			Forest currentSmallest = currentMinValues[0];
			Forest secondcurrentSmallest = currentMinValues[1];

			// *** FINDS INDEX OF SMALLEST OBJECTS BY WEIGHT IN FORESTPARAM ARRAY ***
			int smallestIndex = findIndexByRoot(forestParam, currentSmallest);
			int secondSmallestIndex = findIndexByRoot(forestParam, secondcurrentSmallest);

			// *** GETS OBJECTS FROM THE INDEXES FOUND WITH SMALLEST WEIGHTS FROM forestParam ARRAY ***
			currentSmallest = forestParam[smallestIndex]; 
			secondcurrentSmallest = forestParam[secondSmallestIndex]; 

			// *** CREATING TREE NODE ***
			HuffmanTreeNode node = new HuffmanTreeNode(currentSmallest.getRoot(), secondcurrentSmallest.getRoot(), -1);
			Tree[currentTreeIndex] = node;
			HuffmanTreeNode node1 = Tree[node.getLeft()];  
			HuffmanTreeNode node2 = Tree[node.getRight()]; 
			node1.setParent(currentTreeIndex);
			node2.setParent(currentTreeIndex);
			Tree[node.getLeft()] = node1;
			Tree[node.getRight()] = node2;

			// *** ADDING FREQUENCIES AND SETTING VALUE BACK TO I IN FOREST ARRAY ***
			double frequency = currentSmallest.getfrequency() + secondcurrentSmallest.getfrequency();
			currentSmallest.setfrequency(frequency);
			currentSmallest.setRoot(currentTreeIndex);
			forestParam[smallestIndex] = currentSmallest;

			// *** INCREMENTING J AND REMOVING FROM FOREST ARRAY ***
			secondcurrentSmallest.setfrequency(secondcurrentSmallest.getfrequency() + 1.0);
			forestParam[secondSmallestIndex] = secondcurrentSmallest;

			currentTreeIndex++;
			treesExistInForest = checkIfTreesExist(forestParam);
		}

		return Tree;
	}
	// ********************************* FINISHED CREATING HUFFMAN TREE *********************************

	// ********************************* SETTING CODES FOR HUFFMAN TREE TRAVERSAL *********************************
	/*
	 * i = leaf field from ALPHABET of the selected character
	 * code = “”;
	 * while (parent is not -1) {
	 * Determine the index, p, of the parent of the node at index i
	 * if (lc of node j is equal to i) code = code + 0;
	 * else  code = code + 1;
	 * i = p;
	 * }
	 */
	public static String getCodes (HuffmanTreeNode[] Tree, alphabetRow temp)
	{
		int tableIndex = temp.getLeaf();

		HuffmanTreeNode tempLeafNode = Tree[tableIndex];
		String code = "";
		while(tempLeafNode.getParent() != -1)
		{
			int parentValue = tempLeafNode.getParent();
			HuffmanTreeNode newIndexedNode = Tree[parentValue];

			outerloop: for(int i = 0; i < Tree.length; i++)
			{
				if(tempLeafNode == Tree[i])
				{
					tableIndex = i;
					break outerloop;
				}
			}

			if(newIndexedNode.getLeft() == tableIndex)
			{
				code = code + "0";
			}
			else
			{
				code = code + "1";
			}

			tempLeafNode = Tree[parentValue];
		}

		return code;
	}
	// ********************************* FINISHED SETTING CODES FOR HUFFMAN TREE TRAVERSAL *********************************

	// ********************************* FINISHED WRITING OUT ENCODED STRING AND ALPHABET *********************************
	public static void Zip(List<alphabetRow> myAlphabet, String FileName) throws IOException
	{		
		FileReader fr = new FileReader(FileName);
		Scanner in = new Scanner(fr);

		String EncodedPrivateString = "";

		// *** WHILE TEXT FILE HAS NEXT LINE ***
		while(in.hasNextLine())
		{
			String Line = in.nextLine();

			// *** GETTING CHARACTERS AT EACH LINE ***
			int substringIndex = 0;
			while(substringIndex < Line.length())
			{
				char character = Line.charAt(substringIndex);
				outerloop: for(int j = 0; j < myAlphabet.size(); j++)
				{
					alphabetRow temp = myAlphabet.get(j);
					if(character == temp.getcharFromFile())
					{
						EncodedPrivateString = EncodedPrivateString + temp.getCode();
						break outerloop;
					}
				}
				substringIndex++;
			}

			// *** HANDLES NEW LINES WHEN WRITING OUT TO THE DECODED STRING ***
			if(in.hasNextLine())
			{
				char character = ' ';
				outerloop: for(int j = 0; j < myAlphabet.size(); j++)
				{
					alphabetRow temp = myAlphabet.get(j);
					if(character == temp.getcharFromFile())
					{
						EncodedPrivateString = EncodedPrivateString + temp.getCode();
						break outerloop;
					}
				}
			}

			substringIndex = 0;
		}

		System.out.println("STRING TO BE ENCODED: " + EncodedPrivateString);

		// *** ENCODES THE STRING INTO A TEXR FILE ***
		coding.encode(EncodedPrivateString, "EncodedPrivateString.hzip");

		// *** WRITE OUT THE HUFFMAN REFERENCE ALPHABET TABLE ***
		File file = new File("HuffmanCypher.huff");
		FileWriter writer = null;
		try
		{
			writer = new FileWriter(file);
			for(int i = 0; i < myAlphabet.size(); i++)
			{
				alphabetRow temp = myAlphabet.get(i);
				String obj = temp.toString() + "\n";
				writer.write(obj, 0, obj.length());
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (writer != null)
				try
			{
					writer.close();
			}
			catch (IOException ignore)
			{
			}	
		}
	}
	// ********************************* FINISHED WRITING OUT ENCODED STRING AND ALPHABET FILES *********************************

	// ********************************* DECIPHERING CODE WITH NEW ALPHABET *********************************
	public static String decipherCodes(String codesFromFile) throws FileNotFoundException
	{		
		String characterString = "";
		List <alphabetRow> cypherTable = new ArrayList<alphabetRow>();

		// *** CREATE A LIST FROM THE REFERENCE TABLE ***
		FileReader fr = new FileReader("HuffmanCypher.huff");
		Scanner in = new Scanner(fr);
		while(in.hasNextLine())
		{
			// *** GET THE NEXT LINE ***
			String alphabetCypher = in.nextLine();

			// *** GET THE CHARACTER FROM THE TABLE ***
			char getCharacters = alphabetCypher.charAt(0);

			// *** CUT THE CHARACTERS OUT OF THE STRING ***
			alphabetCypher = alphabetCypher.substring(2);
			alphabetCypher.trim();

			// *** SEPARATE THE FREQUENCY ATTRIBUTES BY SPACE ***
			String[] spaceSlicer = alphabetCypher.split("\\s+");

			// *** MAKE A TEMPORARY alphabetRow ***
			alphabetRow temp = new alphabetRow();

			// *** SET THE ALPHABET CHARACTER ***
			temp.setcharFromFile(getCharacters);

			// *** SET THE CODE FROM THE CODE INDEX OF THE SEPARATED ARRAY ***
			temp.setCode(spaceSlicer[0]);

			// *** SET THE LEAF FROM THE LEAF INDEX OF THE SEPARATED ARRAY ***
			temp.setLeaf(Integer.parseInt(spaceSlicer[1]));

			// *** ADD TEMP OBJECT TO THE NEW REFERENCE TABLE FOUND FROM THE HUFFMAN TABLE FILE ***
			cypherTable.add(temp);
		}

		// *** LOOKING AT CODES FROM FILE ***
		String bitSlice = "";
		for(int i = 0; i < codesFromFile.length(); i++)
		{
			// *** BREAK UP THE ENCODE STRING BY EVERY 8TH CHARACTER ***
			if(i % 8 == 0)
			{					
				bitSlice = codesFromFile.substring(i, i+8);

				// *** CROSS CHECK THE BIT STRING WITH THE CODES FROM YOUR NEW HUFFMANTABLE LIST ***
				char characterAtIndex = ' ';
				int position = 0;
				while(position < cypherTable.size())
				{
					String tableCode = cypherTable.get(position).getCode();
					if(bitSlice.equals(tableCode))
					{
						characterAtIndex = cypherTable.get(position).getcharFromFile();
					}
					position++;
				}

				// *** ADD THAT NEW CHARACTER TO THE DECODED STRING ***
				characterString = characterString + characterAtIndex;
			}

			// *** RESET BITS AND DO IT AGAIN, UNTIL THE LENGTH OF THE ENCODED STRING
			bitSlice = "";
		}

		// *** RETURN THE DECODED STRING ***
		return characterString;
	}
	// ********************************* FINISHED DECIPHERING CODE WITH NEW ALPHABET *********************************
}

import java.rmi.RemoteException;

public class SearchWord {
	private Node queriedNode;
	
	public WordEntry searchForWord(String word, Node qNode)
	{
		queriedNode = qNode;
		GenericKey wKey = new WordKey(word);
		Node responsibleNode = null;
		WordEntry wordEntry = null;
		try
		{

			responsibleNode = queriedNode.findSuccessorNode(wKey);
		}
		catch (Exception ex)
		{
		}

		try
		{
			/*Request the file from the node*/
			//get the word using the key from the responsibleNode
		}
		catch (Exception ex)
		{
		}

		return wordEntry;
	}
}
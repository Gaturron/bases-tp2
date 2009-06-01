package ubadb.common;

public class PageIdentifier
{
	private int tableId;
	private int pageId;
	
	public PageIdentifier(int tableId, int pageId)
	{
		this.tableId = tableId;
		this.pageId = pageId;
	}

	public int getTableId()
	{
		return tableId;
	}

	public int getPageId()
	{
		return pageId;
	}
}

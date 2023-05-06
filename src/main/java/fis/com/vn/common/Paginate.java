package fis.com.vn.common;

public class Paginate {
	private int limit;
	private int page;
	
	public Paginate(String page, String limit) {
        if (page == null || page.equals("0") || page.isEmpty()) {
			page = "1";
		}
        if (limit == null || limit.isEmpty()) {
			limit = "20";
		}
        this.limit = Integer.parseInt(limit);
        this.page = Integer.parseInt(page);
	}

	public int getLimit() {
		return limit;
	}

	public int getPage() {
		return page;
	}
}

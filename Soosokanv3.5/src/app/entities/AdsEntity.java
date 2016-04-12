package app.entities;




	public class AdsEntity{
		private String _id;
		private String sellerId;
		private String title;
		private String description;
		private String time;
		private int distance;
		private String attribute;
		
		
		
		public AdsEntity(String adsId, String sellerID, String title,
				String description, String time, int distance, String attribute) {
			super();
			this._id = adsId;
			this.sellerId = sellerID;
			this.title = title;
			this.description = description;
			this.time = time;
			this.distance = distance;
			this.setAttribute(attribute);
		}

		public String getAdsId() {
			return _id;
		}

		public void setAdsId(String adsId) {
			this._id = adsId;
		}

		public String getSellerID() {
			return sellerId;
		}

		public void setSellerID(String sellerID) {
			this.sellerId = sellerID;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public int getDistance() {
			return distance;
		}

		public void setDistance(int distance) {
			this.distance = distance;
		}

		public String getAttribute() {
			return attribute;
		}

		public void setAttribute(String attribute) {
			this.attribute = attribute;
		}
		public AdsEntity() {
			super();
			// TODO Auto-generated constructor stub
		}
		
	}


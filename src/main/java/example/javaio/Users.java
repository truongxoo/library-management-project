package example.javaio;

public class Users {
	    private String name;
	    private Double age;
	    private String sex;
	    
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Double getAge() {
			return age;
		}
		public void setAge(Double double1) {
			this.age = double1;
		}
		public String getSex() {
			return sex;
		}
		public void setSex(String sex) {
			this.sex = sex;
		}
		
		 
		public Users() {
            super();
        }
        public Users(String name, Double age, String sex) {
			super();
			this.name = name;
			this.age = age;
			this.sex = sex;
		}
        @Override
        public String toString() {
            return "Users [name=" + name + ", age=" + age + ", sex=" + sex + "]";
        }
        
}
 
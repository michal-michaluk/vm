package micro.client;

public class ClientFixture {
    public static class LocationUpdate {
        public static String requestDusartstraat() {
            return "{ \n"
                    + "  \"location\" : {\n"
                    + "    \"city\" : \"Amsterdam\",\n"
                    + "    \"houseNumber\" : \"3\",\n"
                    + "    \"street\" : \"Dusartstraat\",\n"
                    + "    \"zipcode\" : \"1072HS\",\n"
                    + "    \"country\" : \"NLD\",\n"
                    + "    \"longitude\" : 4.809561,\n"
                    + "    \"latitude\" : 52.352206\n"
                    + "  }\n"
                    + "}";
        }
    }
}

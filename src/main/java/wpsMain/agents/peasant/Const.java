package wpsMain.agents.peasant;

public class Const {
    public static class Semantica {

        public static class Personas {
            public static String Vendedor = "Vendedor";
            public static String Comprador = "Comprador";
            public static String Transeunte = "Transeunte";
            public static String Ladron = "Ladron";
        }

        public static class Objetos {
            public static String Producto = "Producto";
            public static String Almacen = "Almacen";
            public static String Sanitario = "Sanitario";
            public static String Alimento = "Alimento";
        }

        public static class Eventos {
            public static String RecibePublicidad = "RecibePublicidad";
            public static String ObservaCompra = "ObservaCompra";
            public static String ObservaRobo = "ObservaRobo";
            public static String ObservaSuciedad = "ObservaSuciedad";
            public static String SeAlimenta = "SeAlimenta";
            public static String UtilizaSaniario = "UtilizaSanitario";
        }

        public static class ValoracionObjetos {
            public static SemanticValue _1_Repulsivo = new SemanticValue("_1_Repulsivo", -1f);
            public static SemanticValue _2_NoValioso = new SemanticValue("_2_No valioso", -0.2f);
            public static SemanticValue _3_Indiferente = new SemanticValue("_3_Indiferente", 0f);
            public static SemanticValue _4_Valioso = new SemanticValue("_4_Valioso", 0.6f);
            public static SemanticValue _5_Importante = new SemanticValue("_5_Importante", 0.8f);
        }

        public static class ValoracionPersonas {
            public static SemanticValue _1_Enemigo = new SemanticValue("_1_Enemigo", -1f);
            public static SemanticValue _2_NoAmigable = new SemanticValue("_2_No amigable", -0.3f);
            public static SemanticValue _3_Desconocido = new SemanticValue("_3_Desconocido", 0f);
            public static SemanticValue _4_Amigo = new SemanticValue("_4_Amigo", 0.7f);
            public static SemanticValue _5_Cercano = new SemanticValue("_5_Cercano", 0.8f);
        }

        public static class ValoracionEventos {
            public static SemanticValue _1_Indeseable = new SemanticValue("_1_Indeseable", -1f);
            public static SemanticValue _2_AlgoIndeseable = new SemanticValue("_2_Algo Indeseable", -0.4f);
            public static SemanticValue _3_Indiferente = new SemanticValue("_3_Indiferente", 0f);
            public static SemanticValue _4_AlgoDeseable = new SemanticValue("_4_Algo Deseable", 0.4f);
            public static SemanticValue _5_Deseable = new SemanticValue("_5_Deseable", 0.1f);
        }

        public static class Emociones {
            public static String Felicidad = "Felicidad";
            public static String Tristeza = "Tristeza";
        }

        public static class Sensaciones {
            public static String Hambre = "Hambre";
            public static String SinHambre = "SinHambre";

            public static String NecesitaSanitario = "NecesitaSanitario";
            public static String NoNecesitaSanitario = "NoNecesitaSanitario";
        }

    }
}

package com.example.triplanproject.Retrofit;

import java.util.List;

public class DirectionResponse {

    private List<Routes> routes;

    public DirectionResponse(List<Routes> routes) {
        this.routes = routes;
    }

    public List<Routes> getRoutes() {
        return routes;
    }

    public class Routes {
        private List<Legs> legs;

        public List<Legs> getLegs() {
            return legs;
        }


        public class Legs {
            private List<Steps> steps;

            public List<Steps> getSteps() {
                return steps;
            }


            public class Steps {
                private Polyline polyline;

                public Polyline getPolyline() {
                    return polyline;
                }


                public class Polyline {
                    private String points;
                    private Duration duration;

                    public String getPoints() {
                        return points;
                    }

                    public Duration getDuration() {
                        return duration;
                    }


                    public class Duration {
                        private String text;

                        public String getText() {
                            return text;
                        }
                    }
                }
            }
        }
    }


}

package com.yahoo.graphql.sample;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import com.coxautodev.graphql.tools.SchemaParser;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import graphql.schema.GraphQLSchema;
import graphql.servlet.GraphQLContext;
import graphql.servlet.SimpleGraphQLServlet;
import graphql.GraphQLException;

@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {

    private static final LinkRepository linkRepository;
    private static final UserRepository userRepository;

    static {
        // Change to `new MongoClient("mongodb://<host>:<port>/hackernews")`
        // if you don't have Mongo running locally on port 27017
        MongoDatabase mongo = new MongoClient().getDatabase("hackernews");
        linkRepository = new LinkRepository(mongo.getCollection("links"));
        userRepository = new UserRepository(mongo.getCollection("users"));
    }
    
    public GraphQLEndpoint() {
        super(buildSchema());
    }

    @Override
    protected GraphQLContext createContext(Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
        User user = request
            .map(req -> req.getHeader("Authorization"))
            .filter(id -> !id.isEmpty())
            .map(id -> id.replace("Bearer ", ""))
            .map(userRepository::findById)
            .orElse(null);

        if ( user == null ) {
            return null;
        }

        return new AuthContext(user, request, response);
    }

    private static GraphQLSchema buildSchema() {
        return SchemaParser.newParser()
                .file("schema.graphqls")
                .resolvers(
                    new Query(linkRepository), 
                    new Mutation(linkRepository, userRepository),
                    new SigninResolver(),
                    new LinkResolver(userRepository)
                )
                .build()
                .makeExecutableSchema();
    }
}

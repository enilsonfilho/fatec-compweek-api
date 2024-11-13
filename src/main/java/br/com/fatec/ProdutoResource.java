package br.com.fatec;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProdutoResource {

    @Operation(
        summary = "Criar produto",
        description = "Esse recurso cadastra um novo produto"
    )
    @APIResponse(
        responseCode = "201",
        description = "Produto cadastrado com sucesso"
    )
    @APIResponse(
        responseCode = "500",
        description = "Erro interno no servidor"
    )
    @Transactional
    @POST
    @Path("/produto")
    public Response criarProduto(@Valid ProdutoDTO produtoDTO) {
        Produto produto = new Produto(produtoDTO.getNome(), produtoDTO.getDescricao(), produtoDTO.getPreco());
        produto.persist();

        return Response.status(Response.Status.CREATED).entity(produto).build();
    }

    @GET
    @Path("/produtos")
    public Response listarProduto() {
        return Response.status(Response.Status.OK).entity(Produto.listAll()).build();
    }

    @Operation(
        summary = "Listar produto",
        description = "Esse lista produtos cadastrados"
    )
    @APIResponse(
        responseCode = "200",
        description = "Produto encontrado com sucesso"
    )
    @APIResponse(
        responseCode = "404",
        description = "Produto não encontrado!"
    )
    @GET
    @Path("/produto/{id}")
    public Response buscarProduto(@PathParam("id") Long id) {
        Produto produto = Produto.findById(id);

        if (produto == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Não possível encontrar o produto com id informado!").build();
        }

        return Response.status(Response.Status.OK).entity(produto).build();
    }

    @Transactional
    @DELETE
    @Path("/produto/{id}")
    public Response deletarProduto(@PathParam("id") Long id) {
        Produto produto = Produto.findById(id);

        if (produto == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Não possível encontrar o produto com id informado!").build();
        }

        Produto.deleteById(id);

        return Response.status(Response.Status.OK).build();
    }

    @Transactional
    @PUT
    @Path("/produto/{id}")
    public Response atualizarProduto(@PathParam("id") Long id, ProdutoDTO produtoDTO) {
        Produto produto = Produto.findById(id);

        if (produto == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Não possível encontrar o produto com id informado!").build();
        }

        produto.setDescricao(produtoDTO.getDescricao());
        produto.setNome(produtoDTO.getNome());
        produto.setPreco(produtoDTO.getPreco());

        Produto.persist(produto);

        return Response.status(Response.Status.OK).entity("Produto atualizado com sucesso!").build();
    }
    
}

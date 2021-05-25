package com.gsantos.minhasfinancas.api.resource;

import com.gsantos.minhasfinancas.api.dto.LancamentoDTO;
import com.gsantos.minhasfinancas.exception.RegraNegocioException;
import com.gsantos.minhasfinancas.model.entity.Lancamento;
import com.gsantos.minhasfinancas.model.enums.StatusLancamento;
import com.gsantos.minhasfinancas.model.enums.TipoLancamento;
import com.gsantos.minhasfinancas.service.LancamentoService;
import com.gsantos.minhasfinancas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoResource {

    private LancamentoService service;
    private UsuarioService usuarioService;

    @Autowired
    public LancamentoResource(LancamentoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {
        try {
            Lancamento entidade = converterParaLancamento(dto);
            entidade = service.salvar(entidade);
            return new ResponseEntity(entidade, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody LancamentoDTO dto) {
        return service.obterPorId(id).map(entity -> {
            try{
                Lancamento lancamento = converterParaLancamento(dto);
                lancamento.setId(entity.getId());
                service.atualizar(lancamento);
                return ResponseEntity.ok(lancamento);
            }
            catch (RegraNegocioException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet( () -> new ResponseEntity("Lancamento não encontrado!", HttpStatus.BAD_REQUEST));


    }


    private Lancamento converterParaLancamento(LancamentoDTO dto) {
        return Lancamento.builder().
                decricao(dto.getDescricao())
                .id(dto.getId())
                .status(StatusLancamento.valueOf(dto.getStatus()))
                .ano(dto.getAno())
                .mes(dto.getMes())
                .valor(dto.getValor())
                .usuario(usuarioService.obterIdPorId(dto.getUsuario())
                        .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o Id informado.")))
                .tipo(TipoLancamento.valueOf(dto.getTipo())).build();

    }
}

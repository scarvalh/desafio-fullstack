package com.accenture.challenge.fullStackChallenge.services;

import com.accenture.challenge.fullStackChallenge.dto.EmpresaDTO;
import com.accenture.challenge.fullStackChallenge.dto.FornecedorDTO;
import com.accenture.challenge.fullStackChallenge.entity.EmpresaEntity;
import com.accenture.challenge.fullStackChallenge.entity.FornecedorEntity;
import com.accenture.challenge.fullStackChallenge.entity.PessoaFisicaEntity;
import com.accenture.challenge.fullStackChallenge.entity.PessoaJuridicaEntity;
import com.accenture.challenge.fullStackChallenge.enums.TiposFornecedor;
import com.accenture.challenge.fullStackChallenge.repository.EmpresaRepository;
import com.accenture.challenge.fullStackChallenge.repository.FornecedorRepository;
import jakarta.transaction.Transactional;
import jakarta.xml.bind.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmpresaService {

    private EmpresaRepository empresaRepository;
    private FornecedorRepository fornecedorRepository;
    private RestTemplate restTemplate;

    public EmpresaService(EmpresaRepository empresaRepository, FornecedorRepository fornecedorRepository){
        this.empresaRepository = empresaRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.restTemplate = new RestTemplate();
    }

    private boolean isParana(String cep) {
        try {
            String url = "https://viacep.com.br/ws/" + cep + "/json/";
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            
            if (response.getBody() != null && response.getBody().get("erro") == null) {
                String uf = (String) response.getBody().get("uf");
                return "PR".equals(uf);
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isCepValido(String cep) {
        try {
            String url = "https://viacep.com.br/ws/" + cep + "/json/";
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            return response.getBody() != null && response.getBody().get("erro") == null;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isMenorDeIdade(java.util.Date dataNascimento) {
        if (dataNascimento == null) return false;
        
        LocalDate dataNasc = dataNascimento.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
        
        LocalDate hoje = LocalDate.now();
        Period periodo = Period.between(dataNasc, hoje);
        
        return periodo.getYears() < 18;
    }

    public EmpresaDTO createEmpresa( EmpresaDTO empresa) throws ValidationException {
        if (empresa.getNome() == null || empresa.getNome().trim().isEmpty()) {
            throw new ValidationException("O nome da empresa é obrigatório");
        }
        if (empresa.getCnpj() == null || empresa.getCnpj().trim().isEmpty()) {
            throw new ValidationException("O CNPJ da empresa é obrigatório");
        }
        if (empresa.getCep() == null || empresa.getCep().trim().isEmpty()) {
            throw new ValidationException("O CEP da empresa é obrigatório");
        }

        if (!isCepValido(empresa.getCep())) {
            throw new ValidationException("CEP inválido");
        }

        if (empresaRepository.findByCNPJ(empresa.getCnpj()).isPresent()) {
            throw new ValidationException("Já existe uma empresa cadastrada com este CNPJ");
        }

        List<FornecedorEntity> fornecedores = new ArrayList<>();
        if(empresa.getFornecedores() != null){
            var fornecedoresIds = empresa.getFornecedores().stream().map(FornecedorDTO::getId).collect(Collectors.toSet());
            fornecedores = fornecedorRepository.findAllById(fornecedoresIds);

            if (fornecedores.size() != fornecedoresIds.size()) {
                throw new ValidationException("Um ou mais fornecedores não foram encontrados");
            }
        }

        boolean isParana = isParana(empresa.getCep());
        
        if (isParana) {
            for (FornecedorEntity fornecedor : fornecedores) {
                if (TiposFornecedor.PF.equals(fornecedor.getTipo())) {
                    PessoaFisicaEntity pf = fornecedor.getPessoaFisica();
                    if (isMenorDeIdade(pf.getDataDeNascimento())) {
                        throw new ValidationException("Empresas do Paraná não podem ter fornecedores pessoa física menores de idade");
                    }
                }
            }
        }

        EmpresaEntity empresaEntity = new EmpresaEntity(null, empresa.getNome(), empresa.getCnpj(), empresa.getCep(), new HashSet<>(fornecedores));
        EmpresaEntity saved = empresaRepository.save(empresaEntity);
        var forneceDoresDTO = saved.getFornecedores().stream().map(FornecedorDTO::new).collect(Collectors.toList());
        return new EmpresaDTO(saved.getId(), saved.getNome(), saved.getCNPJ(), saved.getCEP(), forneceDoresDTO);
    }

    public EmpresaDTO updateEmpresa(EmpresaDTO empresa) throws ValidationException {
        if (empresa.getId() == null) {
            throw new ValidationException("O ID da empresa é obrigatório para atualização");
        }
        if (empresa.getNome() == null || empresa.getNome().trim().isEmpty()) {
            throw new ValidationException("O nome da empresa é obrigatório");
        }
        if (empresa.getCnpj() == null || empresa.getCnpj().trim().isEmpty()) {
            throw new ValidationException("O CNPJ da empresa é obrigatório");
        }
        if (empresa.getCep() == null || empresa.getCep().trim().isEmpty()) {
            throw new ValidationException("O CEP da empresa é obrigatório");
        }

        if (!isCepValido(empresa.getCep())) {
            throw new ValidationException("CEP inválido");
        }

        empresaRepository.findByCNPJ(empresa.getCnpj())
                .orElseThrow(() -> new ValidationException("Já existe uma empresa cadastrada com este CNPJ"));

        EmpresaEntity empresaEntity = empresaRepository.findById(empresa.getId())
                .orElseThrow(() ->new ValidationException("A Empresa não foi encontrada"));

        List<FornecedorEntity> fornecedores = new ArrayList<>();
        if(empresa.getFornecedores() != null){
            var fornecedoresIds = empresa.getFornecedores().stream().map(FornecedorDTO::getId).collect(Collectors.toSet());
            fornecedores = fornecedorRepository.findAllById(fornecedoresIds);

            if (fornecedores.size() != fornecedoresIds.size()) {
                throw new ValidationException("Um ou mais fornecedores não foram encontrados");
            }
        }

        empresaEntity.setNome(empresa.getNome());
        empresaEntity.setCEP(empresa.getCep());
        empresaEntity.setCNPJ(empresa.getCnpj());
        empresaEntity.setFornecedores(new HashSet<>(fornecedores));

        EmpresaEntity saved = empresaRepository.save(empresaEntity);
        var forneceDoresDTO = saved.getFornecedores().stream().map(FornecedorDTO::new).collect(Collectors.toList());
        return new EmpresaDTO(saved.getId(), saved.getNome(), saved.getCNPJ(), saved.getCEP(), forneceDoresDTO);
    }

    public List<EmpresaDTO> findAll(){
        List<EmpresaEntity> empresaList = empresaRepository.findAll();
        return empresaList.stream().map(EmpresaDTO::new).collect(Collectors.toList());
    }

    public EmpresaDTO findByID(Integer id){
        Optional<EmpresaEntity> entityOp = empresaRepository.findById(id);
        return entityOp.map(EmpresaDTO::new).orElse(null);
    }

    public void deleteById(Integer id) throws ValidationException {
        var empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ValidationException("A Empresa não foi encontrada"));

        // Remove todas as relações com fornecedores
        empresa.setFornecedores(new HashSet<>());
        empresaRepository.save(empresa);

        // Agora pode deletar a empresa
        empresaRepository.deleteById(id);
    }

    private FornecedorEntity convertFornecedorToEntity(FornecedorDTO dto){
        PessoaFisicaEntity pfEntity = null;
        PessoaJuridicaEntity pjEntity = null;

        if(TiposFornecedor.PF.getValue().equals(dto.getTipo())){
            pfEntity = new PessoaFisicaEntity(dto.getId(), dto.getDataNascimento(), dto.getRG(), dto.getCPF(), dto.getNome());
        }else {

            pjEntity = new PessoaJuridicaEntity(dto.getId(), dto.getNome(), dto.getCNPJ());
        }

        return new FornecedorEntity(dto.getId(), dto.getEmail(), dto.getCEP(), TiposFornecedor.valueOfEnum(dto.getTipo()), pfEntity, pjEntity);
    }


}

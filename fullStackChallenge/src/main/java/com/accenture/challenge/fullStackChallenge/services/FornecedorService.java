package com.accenture.challenge.fullStackChallenge.services;

import com.accenture.challenge.fullStackChallenge.dto.FornecedorDTO;
import com.accenture.challenge.fullStackChallenge.entity.FornecedorEntity;
import com.accenture.challenge.fullStackChallenge.entity.PessoaFisicaEntity;
import com.accenture.challenge.fullStackChallenge.entity.PessoaJuridicaEntity;
import com.accenture.challenge.fullStackChallenge.enums.TiposFornecedor;
import com.accenture.challenge.fullStackChallenge.repository.FornecedorRepository;
import com.accenture.challenge.fullStackChallenge.repository.PessoaFisicaRepository;
import com.accenture.challenge.fullStackChallenge.repository.PessoaJuridicaRepository;
import jakarta.transaction.Transactional;
import jakarta.xml.bind.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FornecedorService {

    private FornecedorRepository fornecedorRepository;
    private PessoaFisicaRepository pessoaFisicaRepository;
    private PessoaJuridicaRepository pessoaJuridicaRepository;
    private RestTemplate restTemplate;

    public FornecedorService(
            FornecedorRepository fornecedorRepository,
            PessoaJuridicaRepository pessoaJuridicaRepository,
            PessoaFisicaRepository pessoaFisicaRepository) {

        this.fornecedorRepository = fornecedorRepository;
        this.pessoaJuridicaRepository = pessoaJuridicaRepository;
        this.pessoaFisicaRepository = pessoaFisicaRepository;
        this.restTemplate = new RestTemplate();
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

    public FornecedorDTO createFornecedor(FornecedorDTO dto) throws ValidationException {
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new ValidationException("O email é obrigatório");
        }
        if (dto.getCEP() == null || dto.getCEP().trim().isEmpty()) {
            throw new ValidationException("O CEP é obrigatório");
        }

        if (dto.getTipo() == null || dto.getTipo().trim().isEmpty()) {
            throw new ValidationException("O tipo de fornecedor é obrigatório");
        }

        if (!isCepValido(dto.getCEP())) {
            throw new ValidationException("CEP inválido");
        }


        FornecedorEntity fornecedor = null;
        PessoaFisicaEntity pfEntity = null;
        PessoaJuridicaEntity pjEntity = null;
        TiposFornecedor tipo = null;

        if(TiposFornecedor.PF.getValue().equals(dto.getTipo())){
            if(dto.getCPF() == null || dto.getCPF().isEmpty() ||
                    dto.getRG() == null || dto.getRG().isEmpty() ||
                    dto.getDataNascimento() == null){
                throw  new ValidationException("Os seguintes campos são obrigatorios: CPF, RG, Data de Nascimento");
            }
            if(dto.getCNPJ() != null && !dto.getCNPJ().isEmpty()){
                throw  new ValidationException("Nao e' compativel um fornecedor Pessoa Fisica com CNPJ");
            }

            if (pessoaFisicaRepository.findByCPF(dto.getCPF()).isPresent()) {
                throw new ValidationException("Já existe um fornecedor pessoa física cadastrado com este CPF");
            }

            tipo = TiposFornecedor.PF;
            pfEntity = new PessoaFisicaEntity(null, dto.getDataNascimento(), dto.getRG(), dto.getCPF(), dto.getNome());
            pfEntity = pessoaFisicaRepository.save(pfEntity);
        } else {
            if(dto.getCNPJ() == null){
                throw  new ValidationException("Os seguintes campos são obrigatorios: CNPJ");
            }
            if(dto.getCPF() != null && !dto.getCPF().isEmpty()){
                throw  new ValidationException("Nao e' compativel um fornecedor Pessoa Juridica com CPF");
            }

            if (pessoaJuridicaRepository.findByCNPJ(dto.getCNPJ()).isPresent()) {
                throw new ValidationException("Já existe um fornecedor pessoa jurídica cadastrado com este CNPJ");
            }

            tipo = TiposFornecedor.PJ;
            pjEntity = new PessoaJuridicaEntity(null, dto.getNome(), dto.getCNPJ());
            pjEntity = pessoaJuridicaRepository.save(pjEntity);
        }

        if(pfEntity != null && pjEntity != null){
            throw  new ValidationException("O Fornecedor deve ser Pessoa Fisica OU Pessoa Juridica, não se pode cadastrar os dois tipos no mesmo Fornecedor!!");
        }

        fornecedor = new FornecedorEntity(null, dto.getEmail(), dto.getCEP(), tipo, pfEntity, pjEntity);
        return new FornecedorDTO(fornecedorRepository.save(fornecedor));
    }

    public FornecedorDTO updateFornecedor(FornecedorDTO dto) throws ValidationException {
        if (dto.getId() == null) {
            throw new ValidationException("O ID do fornecedor é obrigatório para atualização");
        }
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new ValidationException("O email é obrigatório");
        }
        if (dto.getCEP() == null || dto.getCEP().trim().isEmpty()) {
            throw new ValidationException("O CEP é obrigatório");
        }

        if (dto.getTipo() == null || dto.getTipo().trim().isEmpty()) {
            throw new ValidationException("O tipo de fornecedor é obrigatório");
        }

        if (!isCepValido(dto.getCEP())) {
            throw new ValidationException("CEP inválido");
        }   

        FornecedorEntity fornecedorEntity = fornecedorRepository.findById(dto.getId())
                .orElseThrow(() -> new ValidationException("Fornecedor não encontrado"));

        if(TiposFornecedor.PF.getValue().equals(dto.getTipo())){
            if(dto.getCPF() == null || dto.getCPF().isEmpty() ||
                    dto.getRG() == null || dto.getRG().isEmpty() ||
                    dto.getDataNascimento() == null){
                throw  new ValidationException("Os seguintes campos são obrigatorios: CPF, RG, Data de Nascimento");
            }
            if(dto.getCNPJ() != null && !dto.getCNPJ().isEmpty()){
                throw  new ValidationException("Nao e' compativel um fornecedor Pessoa Fisica com CNPJ");
            }

            pessoaFisicaRepository.findByCPF(dto.getCPF())
                .orElseThrow(() -> new ValidationException("Já existe um fornecedor pessoa física cadastrado com este CPF"));

            PessoaFisicaEntity pfEntity = new PessoaFisicaEntity(
                fornecedorEntity.getPessoaFisica().getId(),
                dto.getDataNascimento(),
                dto.getRG(),
                dto.getCPF(),
                dto.getNome()
            );
            pessoaFisicaRepository.save(pfEntity);
            fornecedorEntity.setPessoaFisica(pfEntity);
            fornecedorEntity.setPessoaJuridica(null);
        } else {
            if(dto.getCNPJ() == null){
                throw  new ValidationException("Os seguintes campos são obrigatorios: CNPJ");
            }
            if(dto.getCPF() != null && !dto.getCPF().isEmpty()){
                throw  new ValidationException("Nao e' compativel um fornecedor Pessoa Juridica com CPF");
            }

            pessoaJuridicaRepository.findByCNPJ(dto.getCNPJ())
                    .orElseThrow(() -> new ValidationException("Já existe um fornecedor pessoa física cadastrado com este CNPJ"));

            PessoaJuridicaEntity pjEntity = new PessoaJuridicaEntity(
                fornecedorEntity.getPessoaJuridica().getId(),
                dto.getNome(),
                dto.getCNPJ()
            );
            pessoaJuridicaRepository.save(pjEntity);
            fornecedorEntity.setPessoaJuridica(pjEntity);
            fornecedorEntity.setPessoaFisica(null);
        }

        fornecedorEntity.setEmail(dto.getEmail());
        fornecedorEntity.setCEP(dto.getCEP());
        fornecedorEntity.setTipo(TiposFornecedor.valueOfEnum(dto.getTipo()));

        return new FornecedorDTO(fornecedorRepository.save(fornecedorEntity));
    }

    public List<FornecedorDTO> findAll(){
        List<FornecedorEntity> list = fornecedorRepository.findAll();
        return  list.stream().map(FornecedorDTO::new).collect(Collectors.toList());
    }

    public FornecedorDTO findById(Integer id){
        Optional<FornecedorEntity> entity = fornecedorRepository.findById(id);
        return entity.map(FornecedorDTO::new).orElse(null);
    }

    public void deleteByID(Integer id) throws ValidationException {
        var fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() ->new ValidationException("O Fornecedor não foi encontrado"));

        // Verifica se o fornecedor está relacionado a alguma empresa
        if (!fornecedor.getEmpresas().isEmpty()) {
            throw new ValidationException("Não é possível excluir o fornecedor pois ele está relacionado a uma ou mais empresas");
        }

        if(TiposFornecedor.PF.equals(fornecedor.getTipo())) {
            pessoaFisicaRepository.deleteById(fornecedor.getPessoaFisica().getId());
        } else pessoaJuridicaRepository.deleteById(fornecedor.getPessoaJuridica().getId());

        fornecedorRepository.deleteById(id);
    }

}




CREATE TYPE analise_status AS ENUM ('PENDENTE', 'PROCESSANDO', 'CONCLUIDA', 'ERRO');

CREATE TABLE analises (
    id BIGSERIAL PRIMARY KEY,
    curriculo_id BIGINT NOT NULL UNIQUE,
    status analise_status NOT NULL DEFAULT 'PENDENTE',
    pontos_fortes_json TEXT,
    pontos_melhoria_json TEXT,
    capacitacoes_json TEXT,
    match_vagas_json TEXT,
    mensagem_erro TEXT,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_analise_curriculo FOREIGN KEY (curriculo_id) REFERENCES curriculos(id) ON DELETE CASCADE
);

CREATE INDEX idx_analises_curriculo_id ON analises(curriculo_id);
CREATE INDEX idx_analises_status ON analises(status);

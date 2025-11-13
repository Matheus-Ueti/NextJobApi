CREATE TABLE IF NOT EXISTS planos (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    categoria VARCHAR(100),
    prioridade VARCHAR(50),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDENTE',
    conteudo_gerado TEXT,
    usuario_id BIGINT NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_plano_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE INDEX idx_plano_usuario ON planos(usuario_id);
CREATE INDEX idx_plano_status ON planos(status);
CREATE INDEX idx_plano_usuario_status ON planos(usuario_id, status);

CREATE TABLE IF NOT EXISTS perfis (
    id BIGSERIAL PRIMARY KEY,
    nome_completo VARCHAR(255) NOT NULL,
    telefone VARCHAR(50),
    linkedin VARCHAR(255),
    github VARCHAR(255),
    portfolio VARCHAR(255),
    usuario_id BIGINT NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_perfil_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE INDEX idx_perfil_usuario ON perfis(usuario_id);

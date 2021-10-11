package br.com.alura.forum.config.validacao;

public class ErroDeFormularioDTO {

	private String campo;
	private String erro;

	public ErroDeFormularioDTO(String campo, String erro) {
		this.campo = campo;
		this.erro = erro;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}

}

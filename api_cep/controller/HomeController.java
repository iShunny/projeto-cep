package com.example.api_cep.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller para a página inicial da API
 * Resolve o erro 500 ao acessar http://localhost:8080/
 */
@Controller
public class HomeController {
    
    /**
     * Endpoint da raiz que exibe uma página HTML com links úteis
     * 
     * @return HTML com informações e links da API
     */
    @GetMapping("/")
    @ResponseBody
    public String home() {
        return """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>API de CEP - Bem-vindo</title>
                <style>
                    * {
                        margin: 0;
                        padding: 0;
                        box-sizing: border-box;
                    }
                    
                    body {
                        font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        min-height: 100vh;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        padding: 20px;
                    }
                    
                    .container {
                        background: white;
                        border-radius: 20px;
                        box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
                        padding: 50px 40px;
                        max-width: 650px;
                        width: 100%;
                        animation: slideUp 0.5s ease-out;
                    }
                    
                    @keyframes slideUp {
                        from {
                            opacity: 0;
                            transform: translateY(30px);
                        }
                        to {
                            opacity: 1;
                            transform: translateY(0);
                        }
                    }
                    
                    .header {
                        text-align: center;
                        margin-bottom: 40px;
                    }
                    
                    h1 {
                        color: #333;
                        font-size: 2.5em;
                        margin-bottom: 10px;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        gap: 15px;
                    }
                    
                    .subtitle {
                        color: #666;
                        font-size: 1.1em;
                        margin-top: 10px;
                    }
                    
                    .status-badge {
                        display: inline-block;
                        background: linear-gradient(135deg, #10b981, #059669);
                        color: white;
                        padding: 10px 25px;
                        border-radius: 25px;
                        font-weight: 600;
                        margin: 20px 0;
                        box-shadow: 0 4px 15px rgba(16, 185, 129, 0.3);
                        animation: pulse 2s infinite;
                    }
                    
                    @keyframes pulse {
                        0%, 100% {
                            transform: scale(1);
                        }
                        50% {
                            transform: scale(1.05);
                        }
                    }
                    
                    .links {
                        display: flex;
                        flex-direction: column;
                        gap: 15px;
                        margin-top: 30px;
                    }
                    
                    .link-card {
                        display: flex;
                        align-items: center;
                        gap: 15px;
                        padding: 20px;
                        background: #f8f9fa;
                        border-radius: 12px;
                        text-decoration: none;
                        color: #333;
                        transition: all 0.3s ease;
                        border: 2px solid transparent;
                    }
                    
                    .link-card:hover {
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        color: white;
                        transform: translateX(10px);
                        box-shadow: 0 10px 25px rgba(102, 126, 234, 0.3);
                        border-color: #667eea;
                    }
                    
                    .link-icon {
                        font-size: 2em;
                        min-width: 50px;
                        text-align: center;
                    }
                    
                    .link-content {
                        flex: 1;
                    }
                    
                    .link-title {
                        font-weight: 600;
                        font-size: 1.1em;
                        margin-bottom: 5px;
                    }
                    
                    .link-description {
                        font-size: 0.9em;
                        opacity: 0.8;
                    }
                    
                    .footer {
                        text-align: center;
                        margin-top: 40px;
                        padding-top: 30px;
                        border-top: 2px solid #e5e7eb;
                        color: #666;
                    }
                    
                    .version {
                        display: inline-block;
                        background: #667eea;
                        color: white;
                        padding: 5px 15px;
                        border-radius: 15px;
                        font-size: 0.9em;
                        margin-top: 10px;
                    }
                    
                    @media (max-width: 600px) {
                        .container {
                            padding: 30px 20px;
                        }
                        
                        h1 {
                            font-size: 1.8em;
                        }
                        
                        .link-card {
                            padding: 15px;
                        }
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>
                            <span>🏠</span>
                            <span>API de CEP</span>
                        </h1>
                        <p class="subtitle">Sistema completo de consulta de endereços brasileiros</p>
                        <div class="status-badge">✅ Sistema Online</div>
                    </div>
                    
                    <div class="links">
                        <a href="/swagger-ui.html" class="link-card">
                            <div class="link-icon">📖</div>
                            <div class="link-content">
                                <div class="link-title">Documentação Swagger</div>
                                <div class="link-description">Interface interativa para testar todos os endpoints</div>
                            </div>
                        </a>
                        
                        <a href="/h2-console" class="link-card">
                            <div class="link-icon">🗄️</div>
                            <div class="link-content">
                                <div class="link-title">Console do Banco H2</div>
                                <div class="link-description">Visualize e gerencie os dados do banco</div>
                            </div>
                        </a>
                        
                        <a href="/v3/api-docs" class="link-card">
                            <div class="link-icon">📡</div>
                            <div class="link-content">
                                <div class="link-title">OpenAPI Specification</div>
                                <div class="link-description">Documentação em formato JSON/YAML</div>
                            </div>
                        </a>
                        
                        <a href="/index.html" class="link-card">
                            <div class="link-icon">🔍</div>
                            <div class="link-content">
                                <div class="link-title">Exemplo de Consulta</div>
                                <div class="link-description">Página de consulta</div>
                            </div>
                        </a>
                    </div>
                    
                    <div class="footer">
                        <p>API REST desenvolvida por João F. da Cruz</p>
                        <span class="version">v1.0.0</span>
                    </div>
                </div>
            </body>
            </html>
            """;
    }
}
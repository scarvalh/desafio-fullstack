# Estágio de build
FROM node:18 as build

# Diretório de trabalho
WORKDIR /app

# Copia os arquivos de configuração
COPY package*.json ./

# Instala as dependências
RUN npm install

# Copia o código fonte
COPY . .

# Compila a aplicação
RUN npm run build

# Estágio de produção
FROM nginx:alpine

# Copia os arquivos compilados para o diretório do nginx
COPY --from=build /app/dist/full-stack-challenge-front/browser /usr/share/nginx/html

# Expõe a porta 80
EXPOSE 80

# Inicia o nginx
CMD ["nginx", "-g", "daemon off;"] 
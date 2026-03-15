# ============================================
# Frontend — Nginx se serve karo
# ============================================
FROM nginx:alpine

# HTML files copy karo
COPY index.html /usr/share/nginx/html/index.html

# Custom Nginx config (CORS + compression)
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=5s \
    CMD wget -q --spider http://localhost:80 || exit 1

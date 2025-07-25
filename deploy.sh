  deploy:
    if: ${{ github.ref == 'refs/heads/main' }}
    runs-on: ubuntu-latest
    needs: docker-build

    steps:
      - name: Checkout repository
        uses: actions/checkout@v3

      - name: Add SSH private key
        run: |
          echo "${{ secrets.SERVER_KEY }}" > key.txt
          chmod 600 key.txt

      - name: Deploy the application
        env:
          SERVER_HOST: ${{ secrets.SERVER_HOST }}   # Ej. 18.211.109.174
          SERVER_PORT: ${{ secrets.SERVER_PORT }}   # Ej. 22
          SERVER_USER: ${{ secrets.SERVER_USER }}   # Ej. ec2-user
        run: |
          set -e
          ./deploy.sh
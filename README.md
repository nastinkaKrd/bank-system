**For google auth** you have to change credentials in application.yml

**Then use url:**
https://accounts.google.com/o/oauth2/v2/auth?
client_id=************&
redirect_uri=https://********.ngrok-free.app/oauth2/callback&
response_type=code&
scope=email%20profile&
access_type=offline

In logs you will see jwt token to have access to this app.

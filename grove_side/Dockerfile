FROM ubuntu 
MAINTAINER warakawason
ADD ButtonToMatchPics/ ButtonToMatchPics/
RUN apt-get update && apt-get install -y \
    python-opencv \
    python-requests \
    python-numpy \
    python-scipy
WORKDIR ButtonToMatchPics

CMD ["python", "match.py"]

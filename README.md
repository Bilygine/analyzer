# Bilygine Analyzer

Bilygine Analyzer can process audio file and products precious data 

# Pre-requisite

- Maven
- Java 8
- Docker

# Setup
Let's go into ***analyzer-core*** directory

    mvn install
    docker-compose run

# Get started

    /** Create steps list */  
    List<Step> steps = new ArrayList<>();
      
    /** Step - 1 | Transcription with Google */  
    steps.add(new GoogleTranscriptionStep("gs://pathToMyFile"));  
      
    /** Analyze **/  
    Analyze analyze = new DefaultAnalyze(steps);  
    analyze.run();
# Roadmap

 - [x] Analyzer Core ***05/10/2018***
 - [x] API With Web spark ***02/10/2018***
 - First steps implementation:	 - [x] Transcription Step implemented with Google Speech to Text ***15/10/2018***
	 - [x] Unique Key associate to each line ***09/11/2018***
	 - [x] Data preparation (Mendatory columns for database ***10/11/2018***
	 - [x] CSV Exportation on Google Storage ***12/11/2018***
 - [x] Firebase synchronization ***14/11/2018***
 - [ ] Unit tests coverage

# Backlog
 - Sound volume processing

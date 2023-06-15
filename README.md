---
description: >-
  Download data from DBpedia Databus with SPARQL and make it data fit for your
  applications using several conversion tools.
---

# Databus Client

The DBpedia Databus Client simplifies data consumption and compilation from the DBpedia Databus, addressing challenges in using data from different publishers and domains. These files are often released in various serialization and compression formats, requiring conversion before they can be utilized. Additionally, data stored in relational databases or community-specific formats necessitates mapping for integration with knowledge graphs. Currently, mapping efforts are dispersed, leading to reduced reusability and unclear provenance.

To address these issues, we propose a software client that can automatically convert and compile data assets registered on a data management platform into formats supported by the target infrastructure. This client enables seamless consumption of compiled data, similar to traditional software dependency management systems. By shifting the burden of format conversion from data providers to the client, we reduce the publishing effort, enhance data consumption with fewer conversion problems, enable data-driven applications with automatically updated dependencies and enhances the findability and reuse of mapping definitions.

The DBpedia Databus Client is a modular and extendable solution that brings us closer to realizing a unified and efficient data ecosystem, promoting reusability and maintaining clear provenance.

#### Example Application Deployment

1. Download the files of 5 datasets as given in the SPARQL query
2. Transform the compression of all files to `.bz2`
3. File format conversion
   1. convert all `RDF` files to `RDF-NTriple` files, and
   2. map the `.tsv` file from the second dataset to `RDF-NTriple` using this `RML-Mapping`, and
   3. use this `XSLT-Mapping` for the `.xml` file in the fifth dataset.
4. Load and deploy the processed data via Docker to a Virtuoso SPARQL Endpoint.

## Important Links

* [**Documentation**](https://dbpedia.gitbook.io/databus/v/download-client/overview/readme)
* [**Source Code**](https://github.com/dbpedia/databus-client/tree/master)
* [**Latest Release**](https://github.com/dbpedia/databus-client/releases/latest)
* [**Discord**](https://discord.gg/fB8byAPP7e)**:** Don't hesitate to ask us, if you have any questions.

## Status

**Beta**: The Databus Client produces expected results for compression conversion and file format conversion. Errors could occure for the mapping process. Please expect some code refactoring and fluctuation.

## Citation

If you use the DBpedia Databus Client in your research, please cite the following paper:

```bibtex
@InProceedings{mcdc2021,
  author = {Johannes Frey and Fabian G\"otz and Marvin Hofer and Sebastian Hellmann},
  title = {Managing and Compiling Data Dependencies for Semantic Applications using Databus Client},
  booktitle = {Metadata and Semantic Research. MTSR 2021}
  year = {2021},
}
```

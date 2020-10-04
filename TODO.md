
1. Reading inputs from Excel.

FeaturesReader has the methods signatures.

A simple reader for reading key/value pairs from excel rows.
Test file included in test/resources flanks-list.xml

Using apache POI.

I have a wrapper around POI that I use in my code:
https://github.com/tzielins/BioDare2-ExcelTools
ModernExcelView is the class to use.
It can read row already as a list of string, which is what you need.

The maven pom is in my Nexus, I will try to add you if I remember how to do it and access it. 

Otherwise you can clone the repo and build locally.


2. Transforming sbol templates into a concrete instances.

TemplateTransformer has the methods signatures.

Start in the order in which they are defined.

The official sbol library cannot be used from maven as it has snapshoot dependencies.
If I make the Nexus work for you you can use the one defined in pom, which I uploaded myself.
If not, download the official fat jar yourself, and then, define in pom to use the local file.

Pleasre raise an issue in their github / official contact so that they either fix their poms
not to use snpshots, or point you to the other repo that contains the code their depend on so we can built snapshot
locally. I should have done it myself long time ago but I forgot.

You should probably read quickly the specification at 
https://sbolstandard.org/wp-content/uploads/2016/06/SBOL2.3.0.pdf

Dont get scare, it is just so you will get the lingo, like component definition and componnent instance, sequence.
Dont read about the "functional parts", stop reading at 7.7 ComponentDefiniton and ignore the rest.

I have been chekcing also the code at: https://github.com/SynBioDex/libSBOLj/tree/master/core2/src/main/java/org/sbolstandard/core2
as it has javadocs.

The transformers methods "bodies" have comments what they should do.

In the tests, the cyano_template.xml is a file that you should upload to SynBioHub to see what the methods shoudl do
as I made examples that ilustrate it.

If you uploaded the files, multiple entries will be created.

The "cyano_codA_Km" is the general template for compounds they made (plasmids).

Using the future transformer code, we could change it into a concreate plasmid in two steps:
- first making copy and renaming it to "sll00199_codA_Km"
- we then replace the left and righ child components with a concrete ones "sll00199_left" that contains real sequences

It gives something like sll00199_codA_Km you can clikc and see the differences.

The last operation, makes the design friendly for other softwares (and also users), so that they can see the sequence details (expand
Sequence Visualisation). It flattens the design and makes one DNA sequence rather than use 4 parts of sequences like in sll00199_codA_Km.

sll00199_codA_Km_flat is the results of such flattening. In which details from "ampr_origin" component are brought into top level 
plasmid.

The CyanoTemplates contains the code with which I created those 3 plasmids. You can check it to understand the idea behind the transformations.
Also it contains all the entities creation code that maybe needed in the methods implementation.






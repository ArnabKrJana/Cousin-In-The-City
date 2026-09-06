package com.oneforth.cousininthecitybackend.service

import org.slf4j.LoggerFactory
import org.springframework.ai.document.Document
import org.springframework.ai.reader.TextReader
import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.transformer.splitter.TokenTextSplitter
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import jakarta.annotation.PostConstruct

@Service
class KnowledgeAgentService(
    private val vectorStore: VectorStore,
    @Value("classpath:docs/mumbai_expat_guide.txt") private val guideResource: Resource
) {
    private val logger = LoggerFactory.getLogger(KnowledgeAgentService::class.java)

    @PostConstruct
    fun ingestData() {
        logger.info("Initializing RAG Pipeline: Ingesting local guides into pgvector...")
        try {
            val textReader = TextReader(guideResource)
            textReader.customMetadata.put("source", "mumbai_guide")

            val documents = textReader.get()
            val textSplitter = TokenTextSplitter.builder().build()
            val splitDocuments = textSplitter.apply(documents)
            
            // Note: In production, you would check if the documents already exist before blindly adding them,
            // but for this demo, pgvector will just overwrite or append.
            vectorStore.add(splitDocuments)
            logger.info("Successfully ingested ${splitDocuments.size} chunks into Vector Store.")
        } catch (e: Exception) {
            logger.error("Failed to ingest document", e)
        }
    }

    @Tool(description = "Search the local knowledge base for cultural rules, unwritten tips, weather, or real-estate advice (like broker fees) for the city.")
    fun searchLocalKnowledge(query: String): String {
        logger.info("LLM triggered RAG Search for: $query")
        
        // Use SearchRequest to strictly limit how much context we send to the LLM (Top-K)
        val searchRequest = SearchRequest.builder()
            .query(query)
            .topK(3) // Only retrieve the top 3 most relevant chunks
            .build()
        
        // Perform semantic similarity search in pgvector
        val results: List<Document> = vectorStore.similaritySearch(searchRequest)
        
        if (results.isEmpty()) {
            return "No relevant cultural or local knowledge found for this query."
        }
        
        // Stitch the chunks together for the LLM context
        return results.joinToString(separator = "\n\n") { it.text ?: "" }
    }
}
